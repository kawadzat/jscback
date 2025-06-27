package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.task.Task;
import io.getarrays.securecapita.task.TaskDto;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenanceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class LaptopService {
    private final UserRepository<User> userRepository;

    private final UserRepository1 userRepository1;

    private final LaptopRepository laptopRepository;


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
        
        laptop.setStatus(newStatus);
        Laptop updatedLaptop = laptopRepository.save(laptop);
        return entityToDto(updatedLaptop);
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
        laptop.setDepartment(dto.getDepartment());
        laptop.setDesignation(dto.getDesignation());
        laptop.setEmail(dto.getEmail());
        laptop.setReplacementDate(dto.getReplacementDate());

        
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
                .department(entity.getDepartment())
                .designation(entity.getDesignation())
                .email(entity.getEmail())
                .replacementDate(entity.getReplacementDate())
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

        // Update the existing laptop with new data
        Laptop updatedLaptop = dtoToEntity(currentUser, existingLaptop, laptopDto);
        updatedLaptop = laptopRepository.save(updatedLaptop);

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
}
