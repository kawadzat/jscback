package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.task.Task;
import io.getarrays.securecapita.task.TaskDto;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenanceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import io.getarrays.securecapita.service.EmailService;

@Service
@AllArgsConstructor
@Transactional
public class LaptopService {
    private final UserRepository<User> userRepository;

    private final UserRepository1 userRepository1;

    private final LaptopRepository laptopRepository;
    private final LaptopAcknowledgmentRepository laptopAcknowledgmentRepository;
    private final SignatureService signatureService;
    private final EmailService emailService;


    public LaptopDto createLaptop(UserDTO currentUser, LaptopDto laptopDto) {
        validateDates(laptopDto);
        Laptop laptopEntity = dtoToEntity(currentUser, null, laptopDto); // Map DTO to entity
        Laptop savedLaptop = laptopRepository.save(laptopEntity);        // Save entity
        return entityToDto(savedLaptop);                                 // Map back to DTO
    }

    private void validateDates(LaptopDto dto) {
        if (dto.getPurchaseDate() == null || dto.getPurchaseDate().getTime() == 0) {
            throw new IllegalArgumentException("Purchase date cannot be null or zero");
        }
        if (dto.getIssueDate() == null || dto.getIssueDate().getTime() == 0) {
            throw new IllegalArgumentException("Issue date cannot be null or zero");
        }
        if (dto.getReplacementDate() == null || dto.getReplacementDate().getTime() == 0) {
            throw new IllegalArgumentException("Replacement date cannot be null or zero");
        }
    }

    public LaptopDto changeLaptopStatus(UserDTO currentUser, Long laptopId, LaptopStatus newStatus) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // If changing to ISSUED status, automatically create pending acknowledgment
        if (newStatus == LaptopStatus.ISSUED) {
            // First set status to PENDING_ACKNOWLEDGMENT
            laptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
            Laptop savedLaptop = laptopRepository.save(laptop);
            
            // Create automatic acknowledgment entry
            createAutomaticAcknowledgment(currentUser, savedLaptop);
            
            return entityToDto(savedLaptop);
        } else {
            // For other status changes, proceed normally
            laptop.setStatus(newStatus);
            Laptop updatedLaptop = laptopRepository.save(laptop);
            return entityToDto(updatedLaptop);
        }
    }

    /**
     * Automatically create a pending acknowledgment when laptop is issued
     * Throws if acknowledgment cannot be created.
     */
    private void createAutomaticAcknowledgment(UserDTO currentUser, Laptop laptop) {
        // Get the current user entity
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Create automatic acknowledgment
        LaptopAcknowledgment acknowledgment = LaptopAcknowledgment.builder()
                .laptop(laptop)
                .acknowledgedBy(user)
                .notes("Automatic acknowledgment created when laptop was issued")
                .signature(null) // No signature for automatic acknowledgment
                .signatureType("AUTOMATIC")
                .signatureTimestamp(LocalDateTime.now())
                .ipAddress("SYSTEM")
                .userAgent("SYSTEM_AUTO_ACKNOWLEDGMENT")
                .certificateInfo("Automatic system acknowledgment")
                .signatureHash(null) // No signature hash for automatic acknowledgment
                .build();
        
        laptopAcknowledgmentRepository.save(acknowledgment);
    }

    private Laptop dtoToEntity(UserDTO currentUser, Laptop existing, LaptopDto dto) {
        Laptop laptop = existing != null ? existing : new Laptop();
        
        // Map basic fields
        laptop.setPurchaseDate(dto.getPurchaseDate());
        laptop.setManufacturer(dto.getManufacturer());
        laptop.setSerialNumber(dto.getSerialNumber());

        laptop.setRam(dto.getRam());
        laptop.setProcessor(dto.getProcessor());
        laptop.setIssueDate(dto.getIssueDate());
        laptop.setStatus(dto.getStatus());
        laptop.setIssuedTo(dto.getIssuedTo());
        laptop.setStation(dto.getStation());
        laptop.setDepartment(dto.getDepartment());
        laptop.setDesignation(dto.getDesignation());
        laptop.setEmail(dto.getEmail());
        laptop.setReplacementDate(dto.getReplacementDate());
        laptop.setIssueByEmail(dto.getIssueByEmail());
        
        return laptop;
    }

    private LaptopDto entityToDto(Laptop entity) {
        LaptopDto dto = LaptopDto.builder()
                .id(entity.getId())
                .purchaseDate(entity.getPurchaseDate())
                .manufacturer(entity.getManufacturer())
                .serialNumber(entity.getSerialNumber())
                .ram(entity.getRam())
                .processor(entity.getProcessor())
                .issueDate(entity.getIssueDate())
                .status(entity.getStatus())
                .issuedTo(entity.getIssuedTo())
                .station(entity.getStation())
                .department(entity.getDepartment())
                .designation(entity.getDesignation())
                .email(entity.getEmail())
                .replacementDate(entity.getReplacementDate())
                .issueByEmail(entity.getIssueByEmail())
                .build();

        // Map maintenances
        if (entity.getMaintenanceList() != null) {
            dto.setMaintenanceList(
                entity.getMaintenanceList().stream()
                    .map(this::maintenanceEntityToDto)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private MaintenanceDto maintenanceEntityToDto(Maintenance maintenance) {
        MaintenanceDto dto = new MaintenanceDto();
        dto.setId(maintenance.getId());
        dto.setDescription(maintenance.getDescription());
        dto.setMaintenanceType(maintenance.getMaintenanceType());
        dto.setStatus(maintenance.getStatus());
        dto.setScheduledDate(maintenance.getScheduledDate());
        dto.setCompletedDate(maintenance.getCompletedDate());
        dto.setTechnicianName(maintenance.getTechnicianName());
        dto.setPriority(maintenance.getPriority());
        dto.setNotes(maintenance.getNotes());
        return dto;
    }

    public LaptopDto updateLaptop(UserDTO currentUser, Long laptopId, LaptopDto laptopDto) {
        // Find existing laptop or throw exception if not found
        Laptop existingLaptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        // Check if status is being changed to ISSUED
        boolean isBeingIssued = laptopDto.getStatus() == LaptopStatus.ISSUED && 
                               existingLaptop.getStatus() != LaptopStatus.ISSUED;

        // Update the existing laptop with new data
        Laptop updatedLaptop = dtoToEntity(currentUser, existingLaptop, laptopDto);
        
        // If being issued, set status to PENDING_ACKNOWLEDGMENT instead of ISSUED
        if (isBeingIssued) {
            updatedLaptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        }
        
        updatedLaptop = laptopRepository.save(updatedLaptop);
        
        // Create automatic acknowledgment if being issued
        if (isBeingIssued) {
            createAutomaticAcknowledgment(currentUser, updatedLaptop);
        }

        return entityToDto(updatedLaptop);
    }

    public List<LaptopDto> getAllLaptops(UserDTO currentUser) {
        List<Laptop> laptops = laptopRepository.findAll();
        return laptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public LaptopDto getLaptopById(UserDTO currentUser, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        return entityToDto(laptop);
    }

    public void deleteLaptop(UserDTO currentUser, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        laptopRepository.delete(laptop);
    }

    public boolean serialNumberExists(String serialNumber) {
        return laptopRepository.existsBySerialNumber(serialNumber);
    }

    public boolean laptopExists(Long laptopId) {
        return laptopRepository.existsById(laptopId);
    }

    /**
     * Update laptop assignment details only
     */
    public LaptopDto updateLaptopAssignment(UserDTO currentUser, Long laptopId, LaptopAssignmentDto assignmentDto) {
        Laptop existingLaptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Update assignment fields only
        existingLaptop.setIssuedTo(assignmentDto.getIssuedTo());
        existingLaptop.setStation(assignmentDto.getStation());
        existingLaptop.setDepartment(assignmentDto.getDepartment());
        existingLaptop.setDesignation(assignmentDto.getDesignation());
        existingLaptop.setEmail(assignmentDto.getEmail());
        
        // If this is a new assignment (laptop was not previously issued), 
        // automatically set status to PENDING_ACKNOWLEDGMENT and create acknowledgment
        if (existingLaptop.getStatus() != LaptopStatus.ISSUED && 
            existingLaptop.getStatus() != LaptopStatus.PENDING_ACKNOWLEDGMENT) {
            existingLaptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
            Laptop savedLaptop = laptopRepository.save(existingLaptop);
            
            // Create automatic acknowledgment entry
            createAutomaticAcknowledgment(currentUser, savedLaptop);
            
            return entityToDto(savedLaptop);
        }
        
        Laptop updatedLaptop = laptopRepository.save(existingLaptop);
        return entityToDto(updatedLaptop);
    }

    /**
     * Update laptop hardware specifications only
     */
    public LaptopDto updateLaptopSpecifications(UserDTO currentUser, Long laptopId, LaptopSpecificationsDto specsDto) {
        Laptop existingLaptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Update specification fields only
        existingLaptop.setRam(specsDto.getRam());
        existingLaptop.setProcessor(specsDto.getProcessor());
        
        Laptop updatedLaptop = laptopRepository.save(existingLaptop);
        return entityToDto(updatedLaptop);
    }

    public List<LaptopDto> getIssuedLaptops(UserDTO currentUser) {
        List<Laptop> issuedLaptops = laptopRepository.findByStatus(LaptopStatus.ISSUED);
        return issuedLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public LaptopDto getLaptopWithIssuedStatus(UserDTO currentUser, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        if (laptop.getStatus() != LaptopStatus.ISSUED) {
            throw new ResourceNotFoundException("Laptop with id " + laptopId + " is not in ISSUED status");
        }
        
        return entityToDto(laptop);
    }

    public LaptopDto getLaptopWithRetiredStatus(UserDTO currentUser, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        if (laptop.getStatus() != LaptopStatus.RETIRED) {
            throw new ResourceNotFoundException("Laptop with id " + laptopId + " is not in RETIRED status");
        }
        
        return entityToDto(laptop);
    }

    public List<LaptopDto> getAllLaptopsWithRetiredStatus(UserDTO currentUser) {
        List<Laptop> retiredLaptops = laptopRepository.findByStatus(LaptopStatus.RETIRED);
        return retiredLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public LaptopDto getLaptopWithRetiredStatusById(UserDTO currentUser, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        if (laptop.getStatus() != LaptopStatus.RETIRED) {
            throw new ResourceNotFoundException("Laptop with id " + laptopId + " is not in RETIRED status");
        }
        
        return entityToDto(laptop);
    }

    public List<LaptopDto> getAllLaptopsWithMaintenanceStatus(UserDTO currentUser) {
        List<Laptop> maintenanceLaptops = laptopRepository.findByStatus(LaptopStatus.MAINTENANCE);
        return maintenanceLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // Issue laptop with pending acknowledgment
    public LaptopDto issueLaptopWithAcknowledgment(UserDTO currentUser, Long laptopId, LaptopDto laptopDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Update laptop with issue details
        laptop = dtoToEntity(currentUser, laptop, laptopDto);
        laptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        
        Laptop savedLaptop = laptopRepository.save(laptop);
        
        // Create automatic acknowledgment entry
        createAutomaticAcknowledgment(currentUser, savedLaptop);
        
        return entityToDto(savedLaptop);
    }

    // Acknowledge laptop issuance by station admin
    public LaptopAcknowledgmentDto acknowledgeLaptopIssuance(UserDTO currentUser, Long laptopId, LaptopAcknowledgmentDto acknowledgmentDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Check if laptop is in pending acknowledgment status
        if (laptop.getStatus() != LaptopStatus.PENDING_ACKNOWLEDGMENT) {
            throw new IllegalArgumentException("Laptop is not in pending acknowledgment status");
        }
        
        // Check if current user is authorized for this station
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!user.isStationAssigned() || !user.getStations().stream()
                .anyMatch(userStation -> userStation.getStation().getStationName().equals(laptop.getStation()))) {
            throw new NotAuthorizedException("You are not authorized to acknowledge laptops for this station");
        }
        
        // Generate signature hash if signature is provided
        String signatureHash = null;
        if (acknowledgmentDto.getSignature() != null && !acknowledgmentDto.getSignature().trim().isEmpty()) {
            signatureHash = signatureService.generateSignatureHash(
                acknowledgmentDto.getSignature(), 
                laptopId, 
                currentUser.getId()
            );
        }
        
        // Create acknowledgment
        LaptopAcknowledgment acknowledgment = LaptopAcknowledgment.builder()
                .laptop(laptop)
                .acknowledgedBy(user)
                .notes(acknowledgmentDto.getNotes())
                .signature(acknowledgmentDto.getSignature())
                .signatureType(acknowledgmentDto.getSignatureType())
                .signatureTimestamp(acknowledgmentDto.getSignatureTimestamp() != null ? acknowledgmentDto.getSignatureTimestamp() : LocalDateTime.now())
                .ipAddress(acknowledgmentDto.getIpAddress())
                .userAgent(acknowledgmentDto.getUserAgent())
                .certificateInfo(acknowledgmentDto.getCertificateInfo())
                .signatureHash(signatureHash)
                .build();
        
        LaptopAcknowledgment savedAcknowledgment = laptopAcknowledgmentRepository.save(acknowledgment);
        
        // Update laptop status to ISSUED
        laptop.setStatus(LaptopStatus.ISSUED);
        laptopRepository.save(laptop);
        
        return acknowledgmentToDto(savedAcknowledgment);
    }

    /**
     * Manually acknowledge a laptop that is in PENDING_ACKNOWLEDGMENT status
     * This converts the status from PENDING_ACKNOWLEDGMENT to ISSUED
     */
    public LaptopDto manuallyAcknowledgeLaptop(UserDTO currentUser, Long laptopId, String acknowledgmentNotes) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Check if laptop is in pending acknowledgment status
        if (laptop.getStatus() != LaptopStatus.PENDING_ACKNOWLEDGMENT) {
            throw new IllegalArgumentException("Laptop is not in pending acknowledgment status");
        }
        
        // Check if current user is authorized for this station
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!user.isStationAssigned() || !user.getStations().stream()
                .anyMatch(userStation -> userStation.getStation().getStationName().equals(laptop.getStation()))) {
            throw new NotAuthorizedException("You are not authorized to acknowledge laptops for this station");
        }
        
        // Update the existing acknowledgment with manual acknowledgment details
        LaptopAcknowledgment existingAcknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("No acknowledgment found for laptop: " + laptopId));
        
        // Update acknowledgment with manual acknowledgment details
        existingAcknowledgment.setNotes(acknowledgmentNotes != null ? acknowledgmentNotes : "Manually acknowledged by station admin");
        existingAcknowledgment.setSignatureType("MANUAL_ACKNOWLEDGMENT");
        existingAcknowledgment.setSignatureTimestamp(LocalDateTime.now());
        existingAcknowledgment.setAcknowledgedBy(user);
        
        laptopAcknowledgmentRepository.save(existingAcknowledgment);
        
        // Update laptop status to ISSUED
        laptop.setStatus(LaptopStatus.ISSUED);
        Laptop updatedLaptop = laptopRepository.save(laptop);
        
        return entityToDto(updatedLaptop);
    }

    // Get laptops pending acknowledgment for a station
    public List<LaptopDto> getLaptopsPendingAcknowledgment(UserDTO currentUser, String stationName) {
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check if user is authorized for this station
        if (!user.isStationAssigned() || !user.getStations().stream()
                .anyMatch(userStation -> userStation.getStation().getStationName().equals(stationName))) {
            throw new NotAuthorizedException("You are not authorized to view laptops for this station");
        }
        
        List<Laptop> pendingLaptops = laptopRepository.findByStatusAndStation(LaptopStatus.PENDING_ACKNOWLEDGMENT, stationName);
        return pendingLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // Get acknowledgment history for a laptop
    public LaptopAcknowledgmentDto getLaptopAcknowledgment(Long laptopId) {
        LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptopId)
                .orElse(null);
        
        if (acknowledgment == null) {
            return null;
        }
        
        return acknowledgmentToDto(acknowledgment);
    }

    private LaptopAcknowledgmentDto acknowledgmentToDto(LaptopAcknowledgment acknowledgment) {
        return LaptopAcknowledgmentDto.builder()
                .id(acknowledgment.getId())
                .laptopId(acknowledgment.getLaptop().getId())
                .acknowledgedBy(io.getarrays.securecapita.dto.UserDTO.toDto(acknowledgment.getAcknowledgedBy()))
                .acknowledgmentDate(acknowledgment.getAcknowledgmentDate())
                .notes(acknowledgment.getNotes())
                .signature(acknowledgment.getSignature())
                .signatureType(acknowledgment.getSignatureType())
                .signatureTimestamp(acknowledgment.getSignatureTimestamp())
                .ipAddress(acknowledgment.getIpAddress())
                .userAgent(acknowledgment.getUserAgent())
                .certificateInfo(acknowledgment.getCertificateInfo())
                .signatureHash(acknowledgment.getSignatureHash())
                .laptopSerialNumber(acknowledgment.getLaptop().getSerialNumber())
                .laptopIssuedTo(acknowledgment.getLaptop().getIssuedTo())
                .laptopStation(acknowledgment.getLaptop().getStation())
                .build();
    }


    // Step 1: Issue the laptop (status becomes PENDING_ACKNOWLEDGMENT)
    public LaptopDto issueLaptop(UserDTO issuer, Long laptopId, LaptopDto laptopDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        // Set status to PENDING_ACKNOWLEDGMENT
        laptop = dtoToEntity(issuer, laptop, laptopDto);
        laptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);

        Laptop savedLaptop = laptopRepository.save(laptop);
        return entityToDto(savedLaptop);
    }

    // Step 2: Acknowledge the laptop (status becomes ISSUED, acknowledgment record created)
    public LaptopAcknowledgmentDto acknowledgeLaptop(UserDTO acknowledger, Long laptopId, String notes) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        // Only allow acknowledgment if status is PENDING_ACKNOWLEDGMENT
        if (laptop.getStatus() != LaptopStatus.PENDING_ACKNOWLEDGMENT) {
            throw new IllegalArgumentException("Laptop is not in PENDING_ACKNOWLEDGMENT status");
        }

        // Create acknowledgment record
        User user = userRepository1.findById(acknowledger.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LaptopAcknowledgment acknowledgment = LaptopAcknowledgment.builder()
                .laptop(laptop)
                .acknowledgedBy(user)
                .notes(notes)
                .signatureType("ACKNOWLEDGMENT")
                .signatureTimestamp(LocalDateTime.now())
                .ipAddress("SYSTEM")
                .userAgent("SYSTEM_ACKNOWLEDGMENT")
                .certificateInfo("Acknowledged by user")
                .build();

        laptopAcknowledgmentRepository.save(acknowledgment);

        // Update status to ISSUED
        laptop.setStatus(LaptopStatus.ISSUED);
        laptopRepository.save(laptop);

        return acknowledgmentToDto(acknowledgment);
    }

    public LaptopDto issueLaptop(UserDTO currentUser, LaptopDto laptopDto) {
        // Check if a laptop with the given serial number already exists
        Optional<Laptop> existingLaptop = laptopRepository.findBySerialNumber(laptopDto.getSerialNumber());
        if (existingLaptop.isPresent()) {
            // Laptop already exists, return the existing one as a DTO
            return existingLaptop.map(this::entityToDto).orElse(null);
        }
        // If not present, create a new laptop
        Laptop laptop = dtoToEntity(currentUser, null, laptopDto);
        // Set the issuer info
        laptop.setIssueByEmail(currentUser.getEmail());
        laptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        Laptop savedLaptop = laptopRepository.save(laptop);
        // Send email notification to the person to whom the laptop is being issued
        String recipientEmail = savedLaptop.getEmail();
        String subject = "Laptop Issued to You";
        String issuerName = currentUser.getFirstName() + " " + currentUser.getLastName();
        String issuerEmail = currentUser.getEmail();
        String confirmUrl = "https://yourdomain.com/laptop/acknowledge?laptopId=" + savedLaptop.getId();
        String message = String.format(
            "Dear %s,<br><br>" +
            "A laptop from <b>%s</b> (Serial Number: %s) has been issued to you by <b>%s</b> (%s). Please acknowledge receipt.<br><br>" +
            "<a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;\">Confirm Receipt</a><br><br>" +
            "Thank you.",
            savedLaptop.getIssuedTo(),
            savedLaptop.getManufacturer(),
            savedLaptop.getSerialNumber(),
            issuerName,
            issuerEmail,
            confirmUrl
        );
        emailService.sendEmail(recipientEmail, subject, message);
        return entityToDto(savedLaptop);
    }



    // Get signature metadata
    public LaptopAcknowledgmentDto getSignatureMetadata(Long laptopId) {
        LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Acknowledgment not found for laptop: " + laptopId));
        
        return acknowledgmentToDto(acknowledgment);
    }

    public List<LaptopDto> getAllAcknowledged() {
        List<Laptop> acknowledgedLaptops = laptopRepository.findByStatus(LaptopStatus.ISSUED);
        return acknowledgedLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public List<LaptopDto> getLaptopsByStatus(LaptopStatus status) {
        List<Laptop> laptops = laptopRepository.findByStatus(status);
        return laptops.stream().map(this::entityToDto).collect(Collectors.toList());
    }


}
