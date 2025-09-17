package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import io.getarrays.securecapita.exception.BadRequestException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.task.Task;
import io.getarrays.securecapita.task.TaskDto;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenanceDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import io.getarrays.securecapita.service.EmailService;

@Service
@AllArgsConstructor
@Slf4j
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
        validateLaptopSpecificFields(laptopDto);
        Laptop laptopEntity = dtoToEntity(currentUser, null, laptopDto); // Map DTO to entity
        
        // WHEN STATUS IS ISSUE, IT MUST GO TO PENDING_ACKNOWLEDGMENT
        if (laptopEntity.getStatus() == LaptopStatus.ISSUE) {
            // ISSUE status automatically becomes PENDING_ACKNOWLEDGMENT
            laptopEntity.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
            Laptop savedLaptop = laptopRepository.save(laptopEntity);
            
            // Create automatic acknowledgment for the issue
            createAutomaticAcknowledgment(currentUser, savedLaptop);
            
            return entityToDto(savedLaptop);
        } 
        // Handle ISSUED status separately if needed
        else if (laptopEntity.getStatus() == LaptopStatus.ISSUED) {
            // For ISSUED status, also go to PENDING_ACKNOWLEDGMENT first
            laptopEntity.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
            Laptop savedLaptop = laptopRepository.save(laptopEntity);
            
            // Create automatic acknowledgment
            createAutomaticAcknowledgment(currentUser, savedLaptop);
            
            return entityToDto(savedLaptop);
        } 
        else {
            // For other statuses, save normally
            Laptop savedLaptop = laptopRepository.save(laptopEntity);
            return entityToDto(savedLaptop);
        }
    }

    private void validateDates(LaptopDto dto) {
        // Only validate purchase date for LAPTOP assets
        if (dto.isLaptopSpecificFieldsRequired()) {
            if (dto.getPurchaseDate() == null || dto.getPurchaseDate().getTime() == 0) {
                throw new IllegalArgumentException("Purchase date cannot be null or zero for LAPTOP assets");
            }
        }
        
        // Only validate issue date for LAPTOP assets
        if (dto.isLaptopSpecificFieldsRequired()) {
            if (dto.getIssueDate() == null || dto.getIssueDate().getTime() == 0) {
                throw new IllegalArgumentException("Issue date cannot be null or zero for LAPTOP assets");
            }
        }
        
        // Only validate replacement date for LAPTOP assets
        if (dto.isLaptopSpecificFieldsRequired()) {
            if (dto.getReplacementDate() == null || dto.getReplacementDate().getTime() == 0) {
                throw new IllegalArgumentException("Replacement date cannot be null or zero for LAPTOP assets");
            }
        }
    }

    private void validateLaptopSpecificFields(LaptopDto dto) {
        // If asset type is LAPTOP, validate all required fields
        if (dto.isLaptopSpecificFieldsRequired()) {
            if (dto.getPurchaseDate() == null) {
                throw new IllegalArgumentException("Purchase date is required for laptop assets");
            }
            if (dto.getManufacturer() == null || dto.getManufacturer().trim().isEmpty()) {
                throw new IllegalArgumentException("Manufacturer is required for laptop assets");
            }
            if (dto.getSerialNumber() == null || dto.getSerialNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Serial number is required for laptop assets");
            }
            if (dto.getRam() == null) {
                throw new IllegalArgumentException("RAM is required for laptop assets");
            }
            if (dto.getProcessor() == null) {
                throw new IllegalArgumentException("Processor is required for laptop assets");
            }
            if (dto.getIssueDate() == null) {
                throw new IllegalArgumentException("Issue date is required for laptop assets");
            }
            if (dto.getStatus() == null) {
                throw new IllegalArgumentException("Status is required for laptop assets");
            }
            // if (dto.getIssuedTo() == null || dto.getIssuedTo().trim().isEmpty()) {
            //     throw new IllegalArgumentException("IssuedTo is required for laptop assets");
            // }
            // if (dto.getStation() == null || dto.getStation().trim().isEmpty()) {
            //     throw new IllegalArgumentException("Station is required for laptop assets");
            // }
            // if (dto.getDepartment() == null || dto.getDepartment().trim().isEmpty()) {
            //     throw new IllegalArgumentException("Department is required for laptop assets");
            // }
            // if (dto.getDesignation() == null || dto.getDesignation().trim().isEmpty()) {
            //     throw new IllegalArgumentException("Designation is required for laptop assets");
            // }
            // if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            //     throw new IllegalArgumentException("Email is required for laptop assets");
            // }
            if (dto.getReplacementDate() == null) {
                throw new IllegalArgumentException("Replacement date is required for laptop assets");
            }
        }
        // For non-laptop assets, all fields are optional
    }

    public LaptopDto changeLaptopStatus(UserDTO currentUser, Long laptopId, LaptopStatus newStatus) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        String previousStatus = laptop.getStatus().toString();
        
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
        laptop.setAssertType(dto.getAssertType());
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
                .assertType(entity.getAssertType())
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

        // Map acknowledgment info
        LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(entity.getId()).orElse(null);
        if (acknowledgment != null) {
            dto.setAcknowledgedBy(io.getarrays.securecapita.dto.UserDTO.toDto(acknowledgment.getAcknowledgedBy()));
            dto.setAcknowledgmentDate(LocalDate.from(acknowledgment.getAcknowledgmentDate()));
            dto.setSignature(acknowledgment.getSignature());
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
        Laptop updatedLaptop = laptopRepository.save(laptop);
        

        
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

        // (Removed station assignment check here)
        
        // Update the existing acknowledgment with manual acknowledgment details
        LaptopAcknowledgment existingAcknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptopId)
                .orElse(null);

        if (existingAcknowledgment == null) {
            existingAcknowledgment = new LaptopAcknowledgment();
            existingAcknowledgment.setLaptop(laptop);
        }
        
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
        String message = String.format(
            "Dear %s,<br><br>" +
            "A laptop from <b>%s</b> (Serial Number: %s) has been issued to you by <b>%s</b> (%s).<br><br>" +
            "Thank you.",
            savedLaptop.getIssuedTo(),
            savedLaptop.getManufacturer(),
            savedLaptop.getSerialNumber(),
            issuerName,
            issuerEmail
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

    public List<LaptopDto> getAllAcknowledgedByStation(String stationName) {
        List<Laptop> acknowledgedLaptops = laptopRepository.findByStatusAndStation(LaptopStatus.ISSUED, stationName);
        return acknowledgedLaptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public List<LaptopDto> getLaptopsByStatus(LaptopStatus status) {
        List<Laptop> laptops = laptopRepository.findByStatus(status);
        return laptops.stream().map(this::entityToDto).collect(Collectors.toList());
    }
    
    public List<LaptopDto> getLaptopsByStation(String station) {
        List<Laptop> laptops = laptopRepository.findByStation(station);
        return laptops.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // Get laptops in acknowledgment state (PENDING_ACKNOWLEDGMENT status) with notes
    public List<LaptopDto> getLaptopsInAcknowledgmentState(UserDTO currentUser) {
        List<Laptop> acknowledgmentLaptops = laptopRepository.findByStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        return acknowledgmentLaptops.stream()
                .map(laptop -> {
                    LaptopDto dto = entityToDto(laptop);
                    // Get acknowledgment notes if available
                    LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptop.getId()).orElse(null);
                    if (acknowledgment != null) {
                        dto.setNotes(acknowledgment.getNotes());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Get laptops pending acknowledgment (overloaded method without station parameter) with notes
    public List<LaptopDto> getLaptopsPendingAcknowledgment(UserDTO currentUser) {
        List<Laptop> pendingLaptops = laptopRepository.findByStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        return pendingLaptops.stream()
                .map(laptop -> {
                    LaptopDto dto = entityToDto(laptop);
                    // Get acknowledgment notes if available
                    LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptop.getId()).orElse(null);
                    if (acknowledgment != null) {
                        dto.setNotes(acknowledgment.getNotes());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Report a laptop issue - automatically sets status to PENDING_ACKNOWLEDGMENT
    public LaptopDto reportLaptopIssue(UserDTO currentUser, Long laptopId, String issueDescription, String priority) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Set status to PENDING_ACKNOWLEDGMENT when issue is reported
        laptop.setStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        
        // Create automatic acknowledgment entry for the issue
        createAutomaticIssueAcknowledgment(currentUser, laptop, issueDescription, priority);
        
        Laptop savedLaptop = laptopRepository.save(laptop);
        return entityToDto(savedLaptop);
    }

    // Create automatic acknowledgment for laptop issues
    private void createAutomaticIssueAcknowledgment(UserDTO currentUser, Laptop laptop, String issueDescription, String priority) {
        try {
            User user = userRepository1.findById(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            LaptopAcknowledgment acknowledgment = LaptopAcknowledgment.builder()
                    .laptop(laptop)
                    .acknowledgedBy(user)
                    .notes("Issue reported: " + issueDescription + " (Priority: " + priority + ")")
                    .signatureType("AUTOMATIC_ISSUE_REPORT")
                    .signatureTimestamp(LocalDateTime.now())
                    .ipAddress("SYSTEM")
                    .userAgent("SYSTEM_ISSUE_REPORT")
                    .certificateInfo("Automatic issue report acknowledgment")
                    .build();

            laptopAcknowledgmentRepository.save(acknowledgment);
        } catch (Exception e) {
            log.error("Failed to create automatic issue acknowledgment for laptop {}: {}", laptop.getId(), e.getMessage());
            // Don't throw exception as this shouldn't block the main operation
        }
    }

    // Get laptops with reported issues (PENDING_ACKNOWLEDGMENT status)
    public List<LaptopDto> getLaptopsWithReportedIssues() {
        List<Laptop> laptopsWithIssues = laptopRepository.findByStatus(LaptopStatus.PENDING_ACKNOWLEDGMENT);
        return laptopsWithIssues.stream()
                .map(laptop -> {
                    LaptopDto dto = entityToDto(laptop);
                    // Get acknowledgment notes if available (contains issue description)
                    LaptopAcknowledgment acknowledgment = laptopAcknowledgmentRepository.findByLaptopId(laptop.getId()).orElse(null);
                    if (acknowledgment != null && acknowledgment.getNotes() != null && 
                        acknowledgment.getNotes().startsWith("Issue reported:")) {
                        dto.setNotes(acknowledgment.getNotes());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Acknowledge a reported laptop issue
    public LaptopAcknowledgmentDto acknowledgeReportedIssue(UserDTO currentUser, Long laptopId, LaptopAcknowledgmentDto acknowledgmentDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        // Check if laptop is in pending acknowledgment status (reported issue)
        if (laptop.getStatus() != LaptopStatus.PENDING_ACKNOWLEDGMENT) {
            throw new BadRequestException("Laptop is not in pending acknowledgment status");
        }
        
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate signature hash if signature is provided
        String signatureHash = null;
        if (acknowledgmentDto.getSignature() != null && !acknowledgmentDto.getSignature().trim().isEmpty()) {
            signatureHash = signatureService.generateSignatureHash(
                acknowledgmentDto.getSignature(), 
                laptopId, 
                currentUser.getId()
            );
        }
        
        // Create acknowledgment for the issue
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
        
        // Update laptop status based on the issue resolution
        // If the issue is resolved, set to AVAILABLE or ISSUED based on context
        // For now, we'll set it to AVAILABLE after acknowledgment
        laptop.setStatus(LaptopStatus.AVAILABLE);
        laptopRepository.save(laptop);
        
        return acknowledgmentToDto(savedAcknowledgment);
    }


}
