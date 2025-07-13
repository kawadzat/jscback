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

    @GetMapping("/{id}/days-to-expiration")
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
    @PutMapping("/{antivirusId}/with-duplicate-check")
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

} 