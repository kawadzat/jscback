package io.getarrays.securecapita.itinventory.SSLCERTIFICATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/sslcertificate")
public class SslCerticateController {
    
    @Autowired
    private SslCerticateService sslCerticateService;

    @GetMapping("/count")
    public ResponseEntity<Long> getSslCertificateCount() {
        long count = sslCerticateService.countSslCertificates();
        return ResponseEntity.ok(count);
    }

    /**
     * Get total lines count (total number of SSL certificate records)
     */
    @GetMapping("/count/totalLines")
    public ResponseEntity<java.util.Map<String, Long>> getTotalLines() {
        long totalLines = sslCerticateService.countSslCertificates();
        return ResponseEntity.ok(java.util.Map.of("totalLines", totalLines));
    }

  




    @PostMapping("/create")
    public ResponseEntity<SslCerticateDto> create(@RequestBody SslCerticateDto dto) {
        return ResponseEntity.ok(sslCerticateService.create(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<SslCerticateDto>> getAll() {
        return ResponseEntity.ok(sslCerticateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SslCerticateDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sslCerticateService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SslCerticateDto> update(@PathVariable Long id, @RequestBody SslCerticateDto dto) {
        return ResponseEntity.ok(sslCerticateService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sslCerticateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
