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
    public Maintenance create(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance update(Maintenance maintenance) {
        if (!maintenanceRepository.existsById(maintenance.getId())) {
            throw new ResourceNotFoundException("Maintenance not found with id: " + maintenance.getId());
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance getById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + id));
    }

    @Override
    public List<Maintenance> getAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance not found with id: " + id);
        }
        maintenanceRepository.deleteById(id);
    }

    @Override
    public Maintenance addMaintenanceToLaptop(Long laptopId, Maintenance maintenance) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        maintenance.setLaptop(laptop);
        
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

    @Override
    public List<Maintenance> getMaintenanceByLaptop(Long laptopId) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        return maintenanceRepository.findByLaptopId(laptopId);
    }

    @Override
    public void removeMaintenanceFromLaptop(Long laptopId, Long maintenanceId) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenanceId));
        
        if (!maintenance.getLaptop().getId().equals(laptopId)) {
            throw new IllegalArgumentException("Maintenance does not belong to the specified laptop");
        }
        
        maintenanceRepository.delete(maintenance);
    }

    @Override
    public Maintenance updateMaintenanceOnLaptop(Long laptopId, Maintenance maintenance) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        
        Maintenance existingMaintenance = maintenanceRepository.findById(maintenance.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenance.getId()));
        
        if (!existingMaintenance.getLaptop().getId().equals(laptopId)) {
            throw new IllegalArgumentException("Maintenance does not belong to the specified laptop");
        }
        
        // Update fields but keep the laptop relationship
        existingMaintenance.setMaintenanceType(maintenance.getMaintenanceType());
        existingMaintenance.setDescription(maintenance.getDescription());
        existingMaintenance.setScheduledDate(maintenance.getScheduledDate());
        existingMaintenance.setCompletedDate(maintenance.getCompletedDate());
        existingMaintenance.setTechnicianName(maintenance.getTechnicianName());

        existingMaintenance.setStatus(maintenance.getStatus());
        existingMaintenance.setPriority(maintenance.getPriority());
        existingMaintenance.setNotes(maintenance.getNotes());
        
        return maintenanceRepository.save(existingMaintenance);
    }

    @Override
    public List<Maintenance> getByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status);
    }

    @Override
    public List<Maintenance> getByMaintenanceType(MaintenanceType maintenanceType) {
        return maintenanceRepository.findByMaintenanceType(maintenanceType);
    }

    @Override
    public List<Maintenance> getByPriority(MaintenancePriority priority) {
        return maintenanceRepository.findByPriority(priority);
    }

    @Override
    public List<Maintenance> getByTechnicianName(String technicianName) {
        return maintenanceRepository.findByTechnicianName(technicianName);
    }

    @Override
    public List<Maintenance> getOverdueMaintenance(LocalDateTime date) {
        return maintenanceRepository.findOverdueMaintenance(date);
    }

    @Override
    public List<Maintenance> getMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRepository.findMaintenanceByDateRange(startDate, endDate);
    }

    @Override
    public List<Maintenance> getCompletedMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRepository.findCompletedMaintenanceByDateRange(startDate, endDate);
    }

    @Override
    public Maintenance scheduleMaintenance(Long laptopId, Maintenance maintenance) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));
        
        maintenance.setLaptop(laptop);
        maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        
        if (maintenance.getPriority() == null) {
            maintenance.setPriority(MaintenancePriority.MEDIUM);
        }
        
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance startMaintenance(Long maintenanceId) {
        Maintenance maintenance = getById(maintenanceId);
        maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance completeMaintenance(Long maintenanceId, String technicianName, String notes) {
        Maintenance maintenance = getById(maintenanceId);
        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        maintenance.setCompletedDate(LocalDateTime.now());
        maintenance.setTechnicianName(technicianName);
        maintenance.setNotes(notes);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance cancelMaintenance(Long maintenanceId, String reason) {
        Maintenance maintenance = getById(maintenanceId);
        maintenance.setStatus(MaintenanceStatus.CANCELLED);
        maintenance.setNotes(reason);
        return maintenanceRepository.save(maintenance);
    }
} 