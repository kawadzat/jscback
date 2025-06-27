package io.getarrays.securecapita.maintenance;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceService {
    Maintenance create(Maintenance maintenance);
    Maintenance update(Maintenance maintenance);
    Maintenance getById(Long id);
    List<Maintenance> getAll();
    void delete(Long id);
    
    // Laptop-related methods
    Maintenance addMaintenanceToLaptop(Long laptopId, Maintenance maintenance);
    List<Maintenance> getMaintenanceByLaptop(Long laptopId);
    void removeMaintenanceFromLaptop(Long laptopId, Long maintenanceId);
    Maintenance updateMaintenanceOnLaptop(Long laptopId, Maintenance maintenance);
    
    // Search and filter methods
    List<Maintenance> getByStatus(MaintenanceStatus status);
    List<Maintenance> getByMaintenanceType(MaintenanceType maintenanceType);
    List<Maintenance> getByPriority(MaintenancePriority priority);
    List<Maintenance> getByTechnicianName(String technicianName);
    List<Maintenance> getOverdueMaintenance(LocalDateTime date);
    List<Maintenance> getMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Maintenance> getCompletedMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Business logic methods
    Maintenance scheduleMaintenance(Long laptopId, Maintenance maintenance);
    Maintenance startMaintenance(Long maintenanceId);
    Maintenance completeMaintenance(Long maintenanceId, String technicianName, String notes);
    Maintenance cancelMaintenance(Long maintenanceId, String reason);
} 