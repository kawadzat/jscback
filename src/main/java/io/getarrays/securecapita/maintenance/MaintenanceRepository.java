package io.getarrays.securecapita.maintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    
    List<Maintenance> findByLaptopId(Long laptopId);
    
    List<Maintenance> findByStatus(MaintenanceStatus status);
    
    List<Maintenance> findByMaintenanceType(MaintenanceType maintenanceType);
    
    List<Maintenance> findByPriority(MaintenancePriority priority);
    
    List<Maintenance> findByTechnicianName(String technicianName);
    
    @Query("SELECT m FROM Maintenance m WHERE m.scheduledDate <= :date AND m.status = 'SCHEDULED'")
    List<Maintenance> findOverdueMaintenance(LocalDateTime date);
    
    @Query("SELECT m FROM Maintenance m WHERE m.scheduledDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'COMPLETED' AND m.completedDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findCompletedMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate);
} 