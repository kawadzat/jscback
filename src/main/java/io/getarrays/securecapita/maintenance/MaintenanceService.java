package io.getarrays.securecapita.maintenance;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceService {

    List<Maintenance> getAllMaintenance();
    
    Maintenance addMaintenanceToLaptopFromDto(Long laptopId, MaintenanceDto maintenanceDto);
    Maintenance updateMaintenanceOnLaptopFromDto(Long laptopId, MaintenanceDto maintenanceDto);
    Maintenance scheduleMaintenanceFromDto(Long laptopId, MaintenanceDto maintenanceDto);

    // Laptop-related methods
    Maintenance addMaintenanceToLaptop(Long laptopId, Maintenance maintenance);

    

} 