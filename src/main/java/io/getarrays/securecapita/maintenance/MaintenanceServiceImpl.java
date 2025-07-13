package io.getarrays.securecapita.maintenance;

import io.getarrays.securecapita.exception.ResourceNotFoundException;

import io.getarrays.securecapita.itinventory.Laptop;
import io.getarrays.securecapita.itinventory.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final LaptopRepository laptopRepository;

    @Override
    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance addMaintenanceToLaptopFromDto(Long laptopId, MaintenanceDto maintenanceDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        Maintenance maintenance = new Maintenance();
        maintenance.setLaptop(laptop);
        
        // Set default maintenance type if not provided
        if (maintenanceDto.getMaintenanceType() == null) {
            maintenance.setMaintenanceType(MaintenanceType.PREVENTIVE);
        } else {
            maintenance.setMaintenanceType(maintenanceDto.getMaintenanceType());
        }
        
        // Set default description if not provided
        if (maintenanceDto.getDescription() == null || maintenanceDto.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        } else {
            maintenance.setDescription(maintenanceDto.getDescription());
        }
        
        // Set default scheduled date if not provided
        if (maintenanceDto.getScheduledDate() == null) {
            maintenance.setScheduledDate(LocalDateTime.now().plusDays(1)); // Default to tomorrow
        } else {
            maintenance.setScheduledDate(maintenanceDto.getScheduledDate());
        }
        
        // Set default status if not provided
        if (maintenanceDto.getStatus() == null) {
            maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        } else {
            maintenance.setStatus(maintenanceDto.getStatus());
        }
        
        // Set default priority if not provided
        if (maintenanceDto.getPriority() == null) {
            maintenance.setPriority(MaintenancePriority.MEDIUM);
        } else {
            maintenance.setPriority(maintenanceDto.getPriority());
        }
        
        // Set other fields
        maintenance.setCompletedDate(maintenanceDto.getCompletedDate());
        maintenance.setTechnicianName(maintenanceDto.getTechnicianName());
        maintenance.setNotes(maintenanceDto.getNotes());
        
        // Final safety check - ensure description is never null or empty
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        }
        
        // Debug logging
        System.out.println("DEBUG: About to save maintenance with description: '" + maintenance.getDescription() + "'");
        System.out.println("DEBUG: Maintenance type: " + maintenance.getMaintenanceType());
        System.out.println("DEBUG: Scheduled date: " + maintenance.getScheduledDate());
        System.out.println("DEBUG: Status: " + maintenance.getStatus());
        System.out.println("DEBUG: Priority: " + maintenance.getPriority());
        
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance updateMaintenanceOnLaptopFromDto(Long laptopId, MaintenanceDto maintenanceDto) {
        // Verify laptop exists
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        
        // Get existing maintenance record
        Maintenance existingMaintenance = maintenanceRepository.findById(maintenanceDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenanceDto.getId()));
        
        // Verify maintenance belongs to the specified laptop
        if (!existingMaintenance.getLaptop().getId().equals(laptopId)) {
            throw new IllegalArgumentException("Maintenance does not belong to the specified laptop");
        }
        
        // Update fields with defaults if not provided
        if (maintenanceDto.getMaintenanceType() != null) {
            existingMaintenance.setMaintenanceType(maintenanceDto.getMaintenanceType());
        }
        
        if (maintenanceDto.getDescription() != null && !maintenanceDto.getDescription().trim().isEmpty()) {
            existingMaintenance.setDescription(maintenanceDto.getDescription());
        }
        
        if (maintenanceDto.getScheduledDate() != null) {
            existingMaintenance.setScheduledDate(maintenanceDto.getScheduledDate());
        }
        
        if (maintenanceDto.getStatus() != null) {
            existingMaintenance.setStatus(maintenanceDto.getStatus());
        }
        
        if (maintenanceDto.getPriority() != null) {
            existingMaintenance.setPriority(maintenanceDto.getPriority());
        }
        
        // Update other fields
        existingMaintenance.setCompletedDate(maintenanceDto.getCompletedDate());
        existingMaintenance.setTechnicianName(maintenanceDto.getTechnicianName());
        existingMaintenance.setNotes(maintenanceDto.getNotes());
        
        return maintenanceRepository.save(existingMaintenance);
    }

    @Override
    public Maintenance scheduleMaintenanceFromDto(Long laptopId, MaintenanceDto maintenanceDto) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        Maintenance maintenance = new Maintenance();
        maintenance.setLaptop(laptop);
        maintenance.setStatus(MaintenanceStatus.SCHEDULED); // Always set to SCHEDULED for this method
        
        // Set default maintenance type if not provided
        if (maintenanceDto.getMaintenanceType() == null) {
            maintenance.setMaintenanceType(MaintenanceType.PREVENTIVE);
        } else {
            maintenance.setMaintenanceType(maintenanceDto.getMaintenanceType());
        }
        
        // Set default description if not provided
        if (maintenanceDto.getDescription() == null || maintenanceDto.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        } else {
            maintenance.setDescription(maintenanceDto.getDescription());
        }
        
        // Set default scheduled date if not provided
        if (maintenanceDto.getScheduledDate() == null) {
            maintenance.setScheduledDate(LocalDateTime.now().plusDays(1)); // Default to tomorrow
        } else {
            maintenance.setScheduledDate(maintenanceDto.getScheduledDate());
        }
        
        // Set default priority if not provided
        if (maintenanceDto.getPriority() == null) {
            maintenance.setPriority(MaintenancePriority.MEDIUM);
        } else {
            maintenance.setPriority(maintenanceDto.getPriority());
        }
        
        // Set other fields
        maintenance.setCompletedDate(maintenanceDto.getCompletedDate());
        maintenance.setTechnicianName(maintenanceDto.getTechnicianName());
        maintenance.setNotes(maintenanceDto.getNotes());
        
        // Final safety check - ensure description is never null or empty
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        }
        
        // Debug logging
        System.out.println("DEBUG: About to save maintenance with description: '" + maintenance.getDescription() + "'");
        System.out.println("DEBUG: Maintenance type: " + maintenance.getMaintenanceType());
        System.out.println("DEBUG: Scheduled date: " + maintenance.getScheduledDate());
        System.out.println("DEBUG: Status: " + maintenance.getStatus());
        System.out.println("DEBUG: Priority: " + maintenance.getPriority());
        
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance addMaintenanceToLaptop(Long laptopId, Maintenance maintenance) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        maintenance.setLaptop(laptop);
        
        // Set default maintenance type if not provided
        if (maintenance.getMaintenanceType() == null) {
            maintenance.setMaintenanceType(MaintenanceType.PREVENTIVE);
        }
        
        // Set default description if not provided
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        }
        
        // Set default scheduled date if not provided
        if (maintenance.getScheduledDate() == null) {
            maintenance.setScheduledDate(LocalDateTime.now().plusDays(1)); // Default to tomorrow
        }
        
        // Only set status to SCHEDULED if it's not already set
        if (maintenance.getStatus() == null) {
            maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        }
        
        // Set default priority if not provided
        if (maintenance.getPriority() == null) {
            maintenance.setPriority(MaintenancePriority.MEDIUM);
        }
        
        return maintenanceRepository.save(maintenance);
    }

} 