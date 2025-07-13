package io.getarrays.securecapita.PurchaseRequest.service;

import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestApprovalDto;
import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestDto;
import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestItemDto;
import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestApprovalHistory;
import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestEntity;
import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestItemEntity;
import io.getarrays.securecapita.PurchaseRequest.entity.TimelineEvent;
import io.getarrays.securecapita.PurchaseRequest.enums.PurchaseRequestStatusEnum;
import io.getarrays.securecapita.PurchaseRequest.repository.PurchaseRequestProductRepo;
import io.getarrays.securecapita.PurchaseRequest.repository.PurchaseRequestRepo;
import io.getarrays.securecapita.asserts.service.StationService;
import io.getarrays.securecapita.codegenerator.CodeGeneratorService;
import io.getarrays.securecapita.department.service.DepartmentService;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.dtomapper.UserDTOMapper;
import io.getarrays.securecapita.exception.BadRequestException;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PurchaseRequestService implements PurchaseRequestServiceInterface {

    private final PurchaseRequestProductRepo purchaseRequestProductRepo;

    private final PurchaseRequestRepo purchaseRequestRepo;

    private final UserRepository<User> userRepository;

    private final CodeGeneratorService codeGeneratorService;

    private final DepartmentService departmentService;

    private final UserRepository1 userRepository1;

    private final EmailService emailService;

    @Override
    public PurchaseRequestDto getPurchaseRequestById(Long purchaseRequestId) {
        PurchaseRequestEntity purchaseRequest =
                purchaseRequestRepo.findById(purchaseRequestId).orElseThrow(() -> new ResourceNotFoundException(
                        "Purchase Request not found with id " + purchaseRequestId));
        return entityToDto(purchaseRequest);
    }

    @Override
    public PageResponseDto<PurchaseRequestDto> getAllPurchaseRequests(PageRequest pageRequest) {
        Page<PurchaseRequestEntity> page = purchaseRequestRepo.findAll(pageRequest);
        return new PageResponseDto<PurchaseRequestDto>(page.getContent().stream().map(e -> entityToDto(e)).toList(),
                page);
    }

    @Override
    public void deletePurchaseRequest(Long purchaseRequestId) {
        getPurchaseRequestById(purchaseRequestId);
        purchaseRequestRepo.deleteById(purchaseRequestId);
    }

    @Override
    public void addProductsToPurchaseRequest(Long id, PurchaseRequestItemEntity purchaseRequestProduct) {

        Optional<PurchaseRequestEntity> purchaseRequestOptional = purchaseRequestRepo.findById(id);

        if (purchaseRequestOptional.isPresent()) {
            PurchaseRequestEntity purchaseRequest = purchaseRequestOptional.get();
            purchaseRequestProduct.setPurchaseRequest(purchaseRequest);
            purchaseRequestProductRepo.save(purchaseRequestProduct);
        }

    }

    @Override
    public void addPurchaseRequestProducttoPurchaseRequest(Long id, PurchaseRequestItemEntity purchaseRequestProduct) {

    }

    @Override
    public Optional<PurchaseRequestEntity> findById(Long id) {
        return purchaseRequestRepo.findById(id);
    }

    @Override
    public PurchaseRequestDto createPurchaseRequest(UserDTO userDTO, PurchaseRequestDto purchaseRequestDto) {
        PurchaseRequestEntity purchaseRequest = new PurchaseRequestEntity();
        purchaseRequest.setDate(purchaseRequestDto.getDate());
        purchaseRequest.setDepartment(departmentService.findDepartmentByIdOrThrow(purchaseRequestDto.getDepartmentId()));
        purchaseRequest.setCode(generateCode(purchaseRequest));
        purchaseRequest.setReason(purchaseRequestDto.getReason());
        final PurchaseRequestEntity pr = purchaseRequest;
        List<PurchaseRequestItemEntity> requestItems = purchaseRequestDto.getRequestItems().stream().map(itemDto -> {
            PurchaseRequestItemEntity item = new PurchaseRequestItemEntity();
            item.setDate(itemDto.getDate());
            item.setRef(itemDto.getRef());
            item.setItemNumber(itemDto.getNumber());
            item.setItemDescription(itemDto.getDescription());
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setEstimatedValue(itemDto.getEstimatedValue());
            item.setQuantity(itemDto.getQuantity());
            item.setPurchaseRequest(pr);
            return item;
        }).collect(Collectors.toList());
        purchaseRequest.setStatus(PurchaseRequestStatusEnum.INITIATED);
        purchaseRequest.setRequestItems(requestItems);
        addApprovalHistory(userDTO, purchaseRequest, purchaseRequestDto.getSignature(),
                PurchaseRequestStatusEnum.INITIATED);
        // Add timeline event for creation
        TimelineEvent timelineEvent = TimelineEvent.builder()
            .purchaseRequest(purchaseRequest)
            .eventType("CREATED")
            .description("Purchase request created by user: " + userDTO.getFirstName() + " " + userDTO.getLastName())
            .eventDate(new Date())
            .user(userRepository1.findByEmail(userDTO.getEmail()).orElse(null))
            .build();
        purchaseRequest.setTimeline(List.of(timelineEvent));
        purchaseRequest = purchaseRequestRepo.save(purchaseRequest);
        sendEmailToNextApprovers(purchaseRequest, userDTO);
        return entityToDto(purchaseRequest);
    }

    private void addApprovalHistory(UserDTO userDto, PurchaseRequestEntity purchaseRequest, String signature,
                                    PurchaseRequestStatusEnum status) {
        List<PurchaseRequestApprovalHistory> approvalHistoryList = purchaseRequest.getApprovalHistories();
        if (approvalHistoryList == null) {
            approvalHistoryList = new ArrayList<>();
        }
        PurchaseRequestApprovalHistory approval = new PurchaseRequestApprovalHistory();
        approval.setDate(new Date());
        approval.setPurchaseRequest(purchaseRequest);
        approval.setStatus(status);
        approval.setUser(userRepository.get(userDto.getId()));
        approval.setSignature(signature);
        approvalHistoryList.add(approval);
        purchaseRequest.setApprovalHistories(approvalHistoryList);
    }

    public PurchaseRequestDto entityToDto(PurchaseRequestEntity purchaseRequestEntity) {
        PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
        purchaseRequestDto.setId(purchaseRequestEntity.getId());
        purchaseRequestDto.setCode(purchaseRequestEntity.getCode());
        purchaseRequestDto.setDate(purchaseRequestEntity.getDate());
        purchaseRequestDto.setDepartmentId(purchaseRequestEntity.getDepartment().getId()); // Assuming Station is
        purchaseRequestDto.setDepartment(departmentService.entityToDto(purchaseRequestEntity.getDepartment()));
        purchaseRequestDto.setReason(purchaseRequestEntity.getReason());

        List<PurchaseRequestItemDto> itemDtos = purchaseRequestEntity.getRequestItems().stream().map(itemEntity -> {
            PurchaseRequestItemDto itemDto = new PurchaseRequestItemDto();
            itemDto.setId(itemEntity.getId());
            itemDto.setDate(itemEntity.getDate());
            itemDto.setRef(itemEntity.getRef());
            itemDto.setNumber(itemEntity.getItemNumber());
            itemDto.setDescription(itemEntity.getItemDescription());
            itemDto.setUnitPrice(itemEntity.getUnitPrice());
            itemDto.setEstimatedValue(itemEntity.getEstimatedValue());
            itemDto.setQuantity(itemEntity.getQuantity());
            return itemDto;
        }).collect(Collectors.toList());

        purchaseRequestDto.setRequestItems(itemDtos);
        purchaseRequestDto.setStatus(purchaseRequestEntity.getStatus().name());

        List<PurchaseRequestApprovalDto> approvals = purchaseRequestEntity.getApprovalHistories().stream().map(e -> {
            PurchaseRequestApprovalDto approvalDto = new PurchaseRequestApprovalDto();
            approvalDto.setDate(e.getDate());
            approvalDto.setSignature(e.getSignature());
            approvalDto.setId(e.getId());
            approvalDto.setUser(UserDTOMapper.fromUser(e.getUser()));
            return approvalDto;
        }).toList();
        purchaseRequestDto.setApprovals(approvals);
        return purchaseRequestDto;
    }

    @Override
    public boolean approvePurchaseRequest(UserDTO currentUser, Long purchaseRequestId,
                                          PurchaseRequestApprovalDto purchaseRequestApprovalDto) throws Exception {
        PurchaseRequestEntity purchaseRequest =
                purchaseRequestRepo.findById(purchaseRequestId).orElseThrow(() -> new ResourceNotFoundException(
                        "Invalid Purchase Request Id: " + purchaseRequestId));

        String userRole = currentUser.getRoleName();
        PurchaseRequestStatusEnum currentStatus = purchaseRequest.getStatus();
        PurchaseRequestStatusEnum nextStatus = null;

        // Validate if the user has the required role to approve the current status
        if (!nextStatus.getRoles().contains(userRole)) {
            throw new NotAuthorizedException("User role not authorized to approve/reject this purchase request");
        }

        String eventType;
        String description;
        if (purchaseRequestApprovalDto.isApprove()) {
            // Ensure user is authorized for the next stage
            if (!nextStatus.getRoles().contains(userRole)) {
                throw new NotAuthorizedException("User role not authorized for the next approval stage");
            }
            eventType = "APPROVED";
            description = "Purchase request approved by user: " + currentUser.getFirstName() + " " + currentUser.getLastName();
        } else { // Handle rejection
            if (currentStatus.equals(PurchaseRequestStatusEnum.RECEIVED)) {
                throw new BadRequestException("Purchase Request is already received and cannot be rejected");
            }
            nextStatus = PurchaseRequestStatusEnum.REJECTED;
            eventType = "REJECTED";
            description = "Purchase request rejected by user: " + currentUser.getFirstName() + " " + currentUser.getLastName();
        }
        purchaseRequest.setStatus(nextStatus);
        addApprovalHistory(currentUser, purchaseRequest, purchaseRequestApprovalDto.getSignature(), nextStatus);
        // Add timeline event for approval/rejection
        List<TimelineEvent> timeline = purchaseRequest.getTimeline();
        if (timeline == null) timeline = new ArrayList<>();
        TimelineEvent timelineEvent = TimelineEvent.builder()
            .purchaseRequest(purchaseRequest)
            .eventType(eventType)
            .description(description)
            .eventDate(new Date())
            .user(userRepository1.findByEmail(currentUser.getEmail()).orElse(null))
            .build();
        timeline.add(timelineEvent);
        purchaseRequest.setTimeline(timeline);
        sendEmailToNextApprovers(purchaseRequestRepo.save(purchaseRequest), currentUser);
        return true;
    }

    private void sendEmailToNextApprovers(PurchaseRequestEntity purchaseRequest, UserDTO currentUser) {
        List<String> nextRoles = purchaseRequest.getStatus().getNext() != null ? purchaseRequest.getStatus().getNext().getRoles() : null;
        if (!CollectionUtils.isEmpty(nextRoles)) {
            userRepository1.findByDepartmentIdAndStationIdInAndRoleNameIn(purchaseRequest.getDepartment().getId(), userRepository.get(currentUser.getId()).getStations().stream().map(e->e.getStation().getStation_id()).toList(), nextRoles).stream().forEach(user -> {
                if (StringUtils.hasText(user.getEmail())) {
                    emailService.sendEmail(user.getEmail(), "Purchase Request " + purchaseRequest.getCode() + " Update", "Purchase Request" + purchaseRequest.getCode() + " is assigned to you and waiting for your approval");
                }
            });
        }
    }

    private String generateCode(PurchaseRequestEntity purchaseRequest) {
        String prefix = purchaseRequest.getDepartment().getName().length() >= 3 ? purchaseRequest.getDepartment().getName().substring(0, 3).toUpperCase() :
                purchaseRequest.getDepartment().getName().toUpperCase();

        int code = codeGeneratorService.generateCode("PurchaseRequest-"+prefix);

        return "PR-" + prefix + "-" + code;
    }

    public PurchaseRequestDto updatePurchaseRequest(UserDTO currentUser, Long purchaseRequestId, PurchaseRequestDto updatedDto) {
        PurchaseRequestEntity purchaseRequest = purchaseRequestRepo.findById(purchaseRequestId)
            .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with id: " + purchaseRequestId));

        // Update fields (example: reason, date, department, items)
        purchaseRequest.setReason(updatedDto.getReason());
        purchaseRequest.setDate(updatedDto.getDate());
        if (updatedDto.getDepartmentId() != null) {
            purchaseRequest.setDepartment(departmentService.findDepartmentByIdOrThrow(updatedDto.getDepartmentId()));
        }
        // Update items if provided
        if (updatedDto.getRequestItems() != null) {
            List<PurchaseRequestItemEntity> requestItems = updatedDto.getRequestItems().stream().map(itemDto -> {
                PurchaseRequestItemEntity item = new PurchaseRequestItemEntity();
                item.setDate(itemDto.getDate());
                item.setRef(itemDto.getRef());
                item.setItemNumber(itemDto.getNumber());
                item.setItemDescription(itemDto.getDescription());
                item.setUnitPrice(itemDto.getUnitPrice());
                item.setEstimatedValue(itemDto.getEstimatedValue());
                item.setQuantity(itemDto.getQuantity());
                return item;
            }).collect(Collectors.toList());
            for (PurchaseRequestItemEntity item : requestItems) {
                item.setPurchaseRequest(purchaseRequest);
            }
            purchaseRequest.setRequestItems(requestItems);
        }
        // Add timeline event for edit
        List<TimelineEvent> timeline = purchaseRequest.getTimeline();
        if (timeline == null) timeline = new ArrayList<>();
        TimelineEvent timelineEvent = TimelineEvent.builder()
            .purchaseRequest(purchaseRequest)
            .eventType("EDITED")
            .description("Purchase request edited by user: " + currentUser.getFirstName() + " " + currentUser.getLastName())
            .eventDate(new Date())
            .user(userRepository1.findByEmail(currentUser.getEmail()).orElse(null))
            .build();
        timeline.add(timelineEvent);
        purchaseRequest.setTimeline(timeline);
        purchaseRequest = purchaseRequestRepo.save(purchaseRequest);
        return entityToDto(purchaseRequest);
    }
}