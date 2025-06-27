package io.getarrays.securecapita.maintenance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/all")
    public ResponseEntity<List<Maintenance>> getAll() {
        return ResponseEntity.ok(maintenanceService.getAll());
    }

    // Basic CRUD operations
    @PostMapping("/create")
    public ResponseEntity<Maintenance> create(@Valid @RequestBody Maintenance maintenance) {
        return new ResponseEntity<>(maintenanceService.create(maintenance), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> update(@PathVariable Long id, @Valid @RequestBody Maintenance maintenance) {
        maintenance.setId(id);
        return ResponseEntity.ok(maintenanceService.update(maintenance));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //ok let me recheck first time to get this error
    @PostMapping("/laptop/{laptopId}")
    public ResponseEntity<Maintenance> addMaintenanceToLaptop(@PathVariable Long laptopId, @Valid @RequestBody Maintenance maintenance) {
        return new ResponseEntity<>(maintenanceService.addMaintenanceToLaptop(laptopId, maintenance), HttpStatus.CREATED);
    }

    @GetMapping("/laptop/{laptopId}")
    public ResponseEntity<List<Maintenance>> getMaintenanceByLaptop(@PathVariable Long laptopId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByLaptop(laptopId));
    }

    @DeleteMapping("/laptop/{laptopId}/maintenance/{maintenanceId}")
    public ResponseEntity<Void> removeMaintenanceFromLaptop(@PathVariable Long laptopId, @PathVariable Long maintenanceId) {
        maintenanceService.removeMaintenanceFromLaptop(laptopId, maintenanceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/laptop/{laptopId}/maintenance/{maintenanceId}")
    public ResponseEntity<Maintenance> updateMaintenanceOnLaptop(@PathVariable Long laptopId, @PathVariable Long maintenanceId, @Valid @RequestBody Maintenance maintenance) {
        maintenance.setId(maintenanceId);
        return ResponseEntity.ok(maintenanceService.updateMaintenanceOnLaptop(laptopId, maintenance));
    }

    // Search and filter endpoints
    @GetMapping("/search/status")
    public ResponseEntity<List<Maintenance>> getByStatus(@RequestParam MaintenanceStatus status) {
        return ResponseEntity.ok(maintenanceService.getByStatus(status));
    }

    @GetMapping("/search/type")
    public ResponseEntity<List<Maintenance>> getByMaintenanceType(@RequestParam MaintenanceType maintenanceType) {
        return ResponseEntity.ok(maintenanceService.getByMaintenanceType(maintenanceType));
    }

    @GetMapping("/search/priority")
    public ResponseEntity<List<Maintenance>> getByPriority(@RequestParam MaintenancePriority priority) {
        return ResponseEntity.ok(maintenanceService.getByPriority(priority));
    }

    @GetMapping("/search/technician")
    public ResponseEntity<List<Maintenance>> getByTechnicianName(@RequestParam String technicianName) {
        return ResponseEntity.ok(maintenanceService.getByTechnicianName(technicianName));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Maintenance>> getOverdueMaintenance(@RequestParam LocalDateTime date) {
        return ResponseEntity.ok(maintenanceService.getOverdueMaintenance(date));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Maintenance>> getMaintenanceByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByDateRange(startDate, endDate));
    }

    @GetMapping("/completed/date-range")
    public ResponseEntity<List<Maintenance>> getCompletedMaintenanceByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(maintenanceService.getCompletedMaintenanceByDateRange(startDate, endDate));
    }

    // Business logic endpoints
    @PostMapping("/schedule/{laptopId}")
    public ResponseEntity<Maintenance> scheduleMaintenance(@PathVariable Long laptopId, @Valid @RequestBody Maintenance maintenance) {
        return new ResponseEntity<>(maintenanceService.scheduleMaintenance(laptopId, maintenance), HttpStatus.CREATED);
    }

    @PutMapping("/{maintenanceId}/start")
    public ResponseEntity<Maintenance> startMaintenance(@PathVariable Long maintenanceId) {
        return ResponseEntity.ok(maintenanceService.startMaintenance(maintenanceId));
    }

    @PutMapping("/{maintenanceId}/complete")
    public ResponseEntity<Maintenance> completeMaintenance(
            @PathVariable Long maintenanceId,
            @RequestParam String technicianName,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(maintenanceService.completeMaintenance(maintenanceId, technicianName, notes));
    }

    @PutMapping("/{maintenanceId}/cancel")
    public ResponseEntity<Maintenance> cancelMaintenance(
            @PathVariable Long maintenanceId,
            @RequestParam String reason) {
        return ResponseEntity.ok(maintenanceService.cancelMaintenance(maintenanceId, reason));
    }

    @GetMapping("/debug/laptop/{laptopId}")
    public ResponseEntity<String> checkLaptopExists(@PathVariable Long laptopId) {
        try {
            maintenanceService.getMaintenanceByLaptop(laptopId);
            return ResponseEntity.ok("Laptop with ID " + laptopId + " exists and can have maintenance records");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
} 