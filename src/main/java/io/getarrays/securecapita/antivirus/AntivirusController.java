package io.getarrays.securecapita.antivirus;

import io.getarrays.securecapita.itinventory.LaptopService;
import io.getarrays.securecapita.itinventory.LaptopDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/antivirus")
@RequiredArgsConstructor
public class AntivirusController {

    private final AntivirusService antivirusService;
    private final LaptopService laptopService;
    // Laptop-related endpoints
    @PostMapping("/laptop/{laptopId}")
    public ResponseEntity<Antivirus> addAntivirusToLaptop(@PathVariable Long laptopId, @RequestBody Antivirus antivirus) {
        return new ResponseEntity<>(antivirusService.addAntivirusToLaptop(laptopId, antivirus), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Antivirus>> getAllAntivirus() {
        return ResponseEntity.ok(antivirusService.getAll());
    }

    // Get all antivirus with laptop information
    @GetMapping("/all-with-laptop-info")
    public ResponseEntity<List<AntivirusDto>> getAllAntivirusWithLaptopInfo() {
        List<Antivirus> antivirusList = antivirusService.getAll();
        List<AntivirusDto> antivirusDtos = antivirusList.stream()
                .map(antivirusService::convertToDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(antivirusDtos);
    }
    
    // Get all antiviruses with detailed laptop information
    @GetMapping("/all-with-detailed-laptop-info")
    public ResponseEntity<List<AntivirusWithDetailedLaptopDto>> getAllAntivirusWithDetailedLaptopInfo() {
        List<Antivirus> antivirusList = antivirusService.getAll();
        List<AntivirusWithDetailedLaptopDto> result = antivirusList.stream()
                .map(this::convertToDetailedDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/id/{id}/days-to-expiration")
    public ResponseEntity<Long> getDaysToExpiration(@PathVariable("id") Long antivirusId) {
        Antivirus antivirus = antivirusService.getById(antivirusId);
        return ResponseEntity.ok(antivirus.getDaysToExpiration());
    }

    // Check if antivirus key exists
    @GetMapping("/check-key/{key}")
    public ResponseEntity<Map<String, Object>> checkKeyExists(@PathVariable String key) {
        boolean exists = antivirusService.isKeyExists(key);
        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("exists", exists);
        response.put("message", exists ? "Key already exists" : "Key is available");
        return ResponseEntity.ok(response);
    }

    // Add antivirus with duplicate check
    @PostMapping("/laptop/{laptopId}/with-duplicate-check")
    public ResponseEntity<Antivirus> addAntivirusWithDuplicateCheck(
            @PathVariable Long laptopId, 
            @RequestBody Antivirus antivirus) {
        try {
            Antivirus saved = antivirusService.addAntivirusToLaptopWithDuplicateCheck(laptopId, antivirus);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Duplicate key error: " + e.getMessage());
        }
    }

    // Update antivirus with duplicate check
    @PutMapping("/id/{antivirusId}/with-duplicate-check")
    public ResponseEntity<Antivirus> updateAntivirusWithDuplicateCheck(
            @PathVariable Long antivirusId, 
            @RequestBody Antivirus antivirus) {
        try {
            Antivirus updated = antivirusService.updateAntivirusWithDuplicateCheck(1L, antivirusId, antivirus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Duplicate key error: " + e.getMessage());
        }
    }

    // Basic update antivirus
    @PutMapping("/id/{antivirusId}")
    public ResponseEntity<Antivirus> updateAntivirus(
            @PathVariable Long antivirusId, 
            @RequestBody Antivirus antivirus) {
        try {
            antivirus.setId(antivirusId);
            Antivirus updated = antivirusService.update(antivirus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Update error: " + e.getMessage());
        }
    }

    // Update antivirus on specific laptop
    @PutMapping("/laptop/{laptopId}/antivirus/{antivirusId}")
    public ResponseEntity<Antivirus> updateAntivirusOnLaptop(
            @PathVariable Long laptopId,
            @PathVariable Long antivirusId, 
            @RequestBody Antivirus antivirus) {
        try {
            antivirus.setId(antivirusId);
            Antivirus updated = antivirusService.updateAntivirusOnLaptop(laptopId, antivirus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Update error: " + e.getMessage());
        }
    }

    // Partial update antivirus (PATCH method)
    @PatchMapping("/id/{antivirusId}")
    public ResponseEntity<Antivirus> partialUpdateAntivirus(
            @PathVariable Long antivirusId, 
            @RequestBody Map<String, Object> updates) {
        try {
            Antivirus existing = antivirusService.getById(antivirusId);
            
            // Apply partial updates
            if (updates.containsKey("name")) {
                existing.setName((String) updates.get("name"));
            }
            if (updates.containsKey("key")) {
                String newKey = (String) updates.get("key");
                if (!existing.getKey().equals(newKey) && antivirusService.isKeyExists(newKey)) {
                    throw new IllegalArgumentException("Antivirus with key '" + newKey + "' already exists");
                }
                existing.setKey(newKey);
            }
            if (updates.containsKey("renewTimeInterval")) {
                existing.setRenewTimeInterval((Integer) updates.get("renewTimeInterval"));
            }
            if (updates.containsKey("version")) {
                existing.setVersion((String) updates.get("version"));
            }
            if (updates.containsKey("vendor")) {
                existing.setVendor((String) updates.get("vendor"));
            }
            if (updates.containsKey("status")) {
                existing.setStatus(AntivirusStatus.valueOf((String) updates.get("status")));
            }
            if (updates.containsKey("isInstalled")) {
                existing.setIsInstalled((Boolean) updates.get("isInstalled"));
            }
            if (updates.containsKey("licenseExpirationDate")) {
                String dateStr = (String) updates.get("licenseExpirationDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    existing.setLicenseExpirationDate(LocalDateTime.parse(dateStr));
                } else {
                    existing.setLicenseExpirationDate(null);
                }
            }
            if (updates.containsKey("lastScanDate")) {
                String dateStr = (String) updates.get("lastScanDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    existing.setLastScanDate(LocalDateTime.parse(dateStr));
                } else {
                    existing.setLastScanDate(null);
                }
            }
            
            Antivirus updated = antivirusService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Partial update error: " + e.getMessage());
        }
    }

    // Update antivirus status only
    @PatchMapping("/id/{antivirusId}/status")
    public ResponseEntity<Antivirus> updateAntivirusStatus(
            @PathVariable Long antivirusId, 
            @RequestBody Map<String, String> statusUpdate) {
        try {
            Antivirus existing = antivirusService.getById(antivirusId);
            String newStatus = statusUpdate.get("status");
            if (newStatus != null) {
                existing.setStatus(AntivirusStatus.valueOf(newStatus.toUpperCase()));
                Antivirus updated = antivirusService.update(existing);
                return ResponseEntity.ok(updated);
            } else {
                throw new IllegalArgumentException("Status field is required");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status update error: " + e.getMessage());
        }
    }

    // Update antivirus installation status
    @PatchMapping("/id/{antivirusId}/installation")
    public ResponseEntity<Antivirus> updateAntivirusInstallation(
            @PathVariable Long antivirusId, 
            @RequestBody Map<String, Boolean> installationUpdate) {
        try {
            Antivirus existing = antivirusService.getById(antivirusId);
            Boolean isInstalled = installationUpdate.get("isInstalled");
            if (isInstalled != null) {
                existing.setIsInstalled(isInstalled);
                Antivirus updated = antivirusService.update(existing);
                return ResponseEntity.ok(updated);
            } else {
                throw new IllegalArgumentException("isInstalled field is required");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Installation update error: " + e.getMessage());
        }
    }

    // Update antivirus license expiration date
    @PatchMapping("/id/{antivirusId}/license-expiration")
    public ResponseEntity<Antivirus> updateLicenseExpiration(
            @PathVariable Long antivirusId, 
            @RequestBody Map<String, String> expirationUpdate) {
        try {
            Antivirus existing = antivirusService.getById(antivirusId);
            String expirationDate = expirationUpdate.get("licenseExpirationDate");
            if (expirationDate != null) {
                if (expirationDate.isEmpty()) {
                    existing.setLicenseExpirationDate(null);
                } else {
                    existing.setLicenseExpirationDate(LocalDateTime.parse(expirationDate));
                }
                Antivirus updated = antivirusService.update(existing);
                return ResponseEntity.ok(updated);
            } else {
                throw new IllegalArgumentException("licenseExpirationDate field is required");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("License expiration update error: " + e.getMessage());
        }
    }

    // Update last scan date
    @PatchMapping("/id/{antivirusId}/last-scan")
    public ResponseEntity<Antivirus> updateLastScanDate(
            @PathVariable Long antivirusId, 
            @RequestBody Map<String, String> scanUpdate) {
        try {
            Antivirus existing = antivirusService.getById(antivirusId);
            String scanDate = scanUpdate.get("lastScanDate");
            if (scanDate != null) {
                if (scanDate.isEmpty()) {
                    existing.setLastScanDate(null);
                } else {
                    existing.setLastScanDate(LocalDateTime.parse(scanDate));
                }
                Antivirus updated = antivirusService.update(existing);
                return ResponseEntity.ok(updated);
            } else {
                throw new IllegalArgumentException("lastScanDate field is required");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Last scan date update error: " + e.getMessage());
        }
    }

    // Delete antivirus
    @DeleteMapping("/id/{antivirusId}")
    public ResponseEntity<Map<String, String>> deleteAntivirus(@PathVariable Long antivirusId) {
        try {
            antivirusService.delete(antivirusId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Antivirus deleted successfully");
            response.put("antivirusId", antivirusId.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Delete error: " + e.getMessage());
        }
    }

    // Remove antivirus from laptop
    @DeleteMapping("/laptop/{laptopId}/antivirus/{antivirusId}")
    public ResponseEntity<Map<String, String>> removeAntivirusFromLaptop(
            @PathVariable Long laptopId,
            @PathVariable Long antivirusId) {
        try {
            antivirusService.removeAntivirusFromLaptop(laptopId, antivirusId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Antivirus removed from laptop successfully");
            response.put("laptopId", laptopId.toString());
            response.put("antivirusId", antivirusId.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Remove error: " + e.getMessage());
        }
    }

    // Get antivirus by name (search)
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Antivirus>> getAntivirusByName(@PathVariable String name) {
        List<Antivirus> antivirusList = antivirusService.getByName(name);
        return ResponseEntity.ok(antivirusList);
    }

    // Get antivirus by vendor
    @GetMapping("/vendor/{vendor}")
    public ResponseEntity<List<Antivirus>> getAntivirusByVendor(@PathVariable String vendor) {
        List<Antivirus> antivirusList = antivirusService.getByVendor(vendor);
        return ResponseEntity.ok(antivirusList);
    }

    // Get antivirus by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Antivirus>> getAntivirusByStatus(@PathVariable String status) {
        AntivirusStatus antivirusStatus = AntivirusStatus.valueOf(status.toUpperCase());
        List<Antivirus> antivirusList = antivirusService.getByStatus(antivirusStatus);
        return ResponseEntity.ok(antivirusList);
    }

    // Get installed antivirus only
    @GetMapping("/installed")
    public ResponseEntity<List<Antivirus>> getInstalledAntivirus() {
        List<Antivirus> antivirusList = antivirusService.getInstalledAntivirus();
        return ResponseEntity.ok(antivirusList);
    }

    // Get expiring licenses
    @GetMapping("/expiring")
    public ResponseEntity<List<Antivirus>> getExpiringLicenses() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(30); // Next 30 days
        List<Antivirus> antivirusList = antivirusService.getExpiringLicenses(futureDate);
        return ResponseEntity.ok(antivirusList);
    }

    // Get expiring licenses with custom days
    @GetMapping("/expiring/{days}")
    public ResponseEntity<List<Antivirus>> getExpiringLicensesInDays(@PathVariable Integer days) {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(days);
        List<Antivirus> antivirusList = antivirusService.getExpiringLicenses(futureDate);
        return ResponseEntity.ok(antivirusList);
    }

    // Get antivirus statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getAntivirusStatistics() {
        List<Antivirus> allAntivirus = antivirusService.getAll();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalAntivirus", allAntivirus.size());
        statistics.put("installedCount", allAntivirus.stream().filter(a -> Boolean.TRUE.equals(a.getIsInstalled())).count());
        statistics.put("activeCount", allAntivirus.stream().filter(a -> AntivirusStatus.ACTIVE.equals(a.getStatus())).count());
        statistics.put("expiredCount", allAntivirus.stream().filter(a -> a.getLicenseExpirationDate() != null && 
            LocalDateTime.now().isAfter(a.getLicenseExpirationDate())).count());
        statistics.put("expiringSoonCount", allAntivirus.stream().filter(a -> a.getLicenseExpirationDate() != null && 
            a.getLicenseExpirationDate().isAfter(LocalDateTime.now()) && 
            a.getLicenseExpirationDate().isBefore(LocalDateTime.now().plusDays(30))).count());
        
        return ResponseEntity.ok(statistics);
    }

    // Get antivirus by key
    @GetMapping("/key/{key}")
    public ResponseEntity<Antivirus> getByKey(@PathVariable String key) {
        Antivirus antivirus = antivirusService.getByKey(key);
        return ResponseEntity.ok(antivirus);
    }

    // Get laptop with its antivirus
                                                    @GetMapping("/laptop/{laptopId}/with-antivirus")
    public ResponseEntity<LaptopWithAntivirusDto> getLaptopWithAntivirus(@PathVariable Long laptopId) {
        // Get laptop DTO
        LaptopDto laptopDto = laptopService.getLaptopById(null, laptopId);
        
        // Get antivirus DTOs for the laptop
        List<AntivirusDto> antivirusDtos = antivirusService.getAntivirusDtosByLaptop(laptopId);
        
        // Create combined DTO
        LaptopWithAntivirusDto result = new LaptopWithAntivirusDto(laptopDto, antivirusDtos);
        
        return ResponseEntity.ok(result);
    }

    // Get all laptops with their antivirus
    @GetMapping("/laptops/with-antivirus")
    public ResponseEntity<List<LaptopWithAntivirusDto>> getAllLaptopsWithAntivirus() {
        // Get all laptops
        List<LaptopDto> laptops = laptopService.getAllLaptops(null);
        
        // Create combined DTOs for each laptop
        List<LaptopWithAntivirusDto> result = laptops.stream()
                .map(laptop -> {
                    List<AntivirusDto> antivirusDtos = antivirusService.getAntivirusDtosByLaptop(laptop.getId());
                    return new LaptopWithAntivirusDto(laptop, antivirusDtos);
                })
                .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(result);
    }

    // Get antivirus by laptop serial number
    @GetMapping("/laptop/serial/{serialNumber}")
    public ResponseEntity<List<AntivirusDto>> getAntivirusByLaptopSerialNumber(@PathVariable String serialNumber) {
        List<AntivirusDto> antivirusList = antivirusService.getAntivirusDtosByLaptopSerialNumber(serialNumber);
        return ResponseEntity.ok(antivirusList);
    }

    // Convert Antivirus to detailed DTO with laptop information
    private AntivirusWithDetailedLaptopDto convertToDetailedDto(Antivirus antivirus) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = antivirus.getLicenseExpirationDate();
        
        // Calculate license status
        Boolean isExpired = expirationDate != null && now.isAfter(expirationDate);
        Long daysUntilExpiration = null;
        String urgencyLevel = "NORMAL";
        
        if (expirationDate != null) {
            if (isExpired) {
                daysUntilExpiration = java.time.Duration.between(expirationDate, now).toDays();
                urgencyLevel = "CRITICAL";
            } else {
                daysUntilExpiration = java.time.Duration.between(now, expirationDate).toDays();
                if (daysUntilExpiration <= 30) {
                    urgencyLevel = "HIGH";
                } else if (daysUntilExpiration <= 90) {
                    urgencyLevel = "MEDIUM";
                }
            }
        }
        
        return AntivirusWithDetailedLaptopDto.builder()
                // Antivirus Information
                .antivirusId(antivirus.getId())
                .antivirusName(antivirus.getName())
                .antivirusKey(antivirus.getKey())
                .renewTimeInterval(antivirus.getRenewTimeInterval())
                .version(antivirus.getVersion())
                .vendor(antivirus.getVendor())
                .status(antivirus.getStatus())
                .isInstalled(antivirus.getIsInstalled())
                .licenseExpirationDate(antivirus.getLicenseExpirationDate())
                .lastScanDate(antivirus.getLastScanDate())
                .daysToExpiration(antivirus.getDaysToExpiration())
                
                // Laptop Information
                .laptopId(antivirus.getLaptop() != null ? antivirus.getLaptop().getId() : null)
                .laptopSerialNumber(antivirus.getLaptop() != null ? antivirus.getLaptop().getSerialNumber() : null)
                .laptopManufacturer(antivirus.getLaptop() != null ? antivirus.getLaptop().getManufacturer() : null)
                .laptopAssertType(antivirus.getLaptop() != null ? antivirus.getLaptop().getAssertType() : null)
                .laptopRam(antivirus.getLaptop() != null ? antivirus.getLaptop().getRam() : null)
                .laptopProcessor(antivirus.getLaptop() != null ? antivirus.getLaptop().getProcessor() : null)
                .laptopPurchaseDate(antivirus.getLaptop() != null ? antivirus.getLaptop().getPurchaseDate() : null)
                .laptopIssueDate(antivirus.getLaptop() != null ? antivirus.getLaptop().getIssueDate() : null)
                .laptopStatus(antivirus.getLaptop() != null && antivirus.getLaptop().getStatus() != null ? 
                    antivirus.getLaptop().getStatus().name() : null)
                .laptopIssuedTo(antivirus.getLaptop() != null ? antivirus.getLaptop().getIssuedTo() : null)
                .laptopStation(antivirus.getLaptop() != null ? antivirus.getLaptop().getStation() : null)
                .laptopDepartment(antivirus.getLaptop() != null ? antivirus.getLaptop().getDepartment() : null)
                .laptopDesignation(antivirus.getLaptop() != null ? antivirus.getLaptop().getDesignation() : null)
                .laptopEmail(antivirus.getLaptop() != null ? antivirus.getLaptop().getEmail() : null)
                .laptopReplacementDate(antivirus.getLaptop() != null ? antivirus.getLaptop().getReplacementDate() : null)
                .laptopIssueByEmail(antivirus.getLaptop() != null ? antivirus.getLaptop().getIssueByEmail() : null)
                
                // Computed fields
                .isLicenseExpired(isExpired)
                .licenseStatus(isExpired ? "EXPIRED" : (daysUntilExpiration != null && daysUntilExpiration <= 30 ? "EXPIRING_SOON" : "ACTIVE"))
                .daysUntilExpiration(daysUntilExpiration)
                .urgencyLevel(urgencyLevel)
                .formattedExpirationDate(expirationDate != null ? expirationDate.toString() : null)
                .formattedLastScanDate(antivirus.getLastScanDate() != null ? antivirus.getLastScanDate().toString() : null)
                .build();
    }

} 