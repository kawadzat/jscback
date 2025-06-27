package io.getarrays.securecapita.productsrequests.service;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.productsrequests.dto.ProductRequestDto;
import io.getarrays.securecapita.productsrequests.dto.ProductRequestItemDto;
import io.getarrays.securecapita.productsrequests.dto.ProductrequestResponsePage;
import io.getarrays.securecapita.productsrequests.entity.ProductRequestEntity;
import io.getarrays.securecapita.productsrequests.entity.ProductRequestItemEntity;
import io.getarrays.securecapita.productsrequests.enums.ProductRequestStatus;
import io.getarrays.securecapita.productsrequests.repository.ProductRequestItemRepository;
import io.getarrays.securecapita.productsrequests.repository.ProductRequestRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.AUTH_ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRequestService {
    private final ProductRequestItemRepository productRequestItemRepository;
    private final ProductRequestRepository productRequestRepository;
    private final UserRepository1 userRepository1;

    public ResponseEntity<?> requestProduct(ProductRequestDto requestMasterDto) {
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository1.findById(userDTO.getId()).get();
        List<ProductRequestItemEntity> productRequestItemEntityList = ProductRequestItemDto.fromDtoList(requestMasterDto.getProducts());
        ProductRequestEntity productRequestEntity = requestMasterDto.toEntity();
        productRequestEntity.setProducts(productRequestItemRepository.saveAll(productRequestItemEntityList));
        productRequestEntity.setInitiator(user);
        productRequestEntity.setStatus(ProductRequestStatus.CREATED);
        productRequestEntity.setRoleStage(AUTH_ROLE.valueOf(user.getRoles().stream().findFirst().get().getRole().getName()));
        productRequestEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        productRequestEntity.setUpdatedDate(productRequestEntity.getCreatedDate());
        ProductRequestEntity savedProductRequest = productRequestRepository.save(productRequestEntity);
        return ResponseEntity.ok(new CustomMessage("Request created successfully."));
    }

    public ResponseEntity<?> getAllRequests(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("updatedDate"));
        Page<ProductRequestEntity> productRequestPage = productRequestRepository.findAll(pageRequest);
        return ResponseEntity.ok(ProductrequestResponsePage.builder()
                .next(productRequestPage.hasNext())
                .previous(productRequestPage.hasPrevious())
                .total(productRequestPage.getTotalElements())
                .size(size)
                .page(page)
                .requests(ProductRequestDto.fromEntities(productRequestPage.getContent()))
                .build());
    }
}
