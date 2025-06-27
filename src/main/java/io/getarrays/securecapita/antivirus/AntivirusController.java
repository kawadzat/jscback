package io.getarrays.securecapita.antivirus;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/antivirus")
@RequiredArgsConstructor
public class AntivirusController {

    private final AntivirusService antivirusService;
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

} 