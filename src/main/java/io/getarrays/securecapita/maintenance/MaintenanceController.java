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

    // Get all maintenance records
    @GetMapping("/all")
    public ResponseEntity<List<Maintenance>> getAllMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenance());
    }

    //ok let me recheck first time to get this error
    @PostMapping("/laptop/{laptopId}")
    public ResponseEntity<Maintenance> addMaintenanceToLaptop(@PathVariable Long laptopId, @Valid @RequestBody MaintenanceDto maintenanceDto) {
        maintenanceDto.setLaptopId(laptopId);
        return new ResponseEntity<>(maintenanceService.addMaintenanceToLaptopFromDto(laptopId, maintenanceDto), HttpStatus.CREATED);
    }

} 