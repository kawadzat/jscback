package io.getarrays.securecapita.itinventory.SSLCERTIFICATE;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ssl-certificate/notifications")
@AllArgsConstructor
@Slf4j
public class SslCertificateNotificationController {

    private final SslCertificateNotificationService notificationService;

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> manuallyCheckAndNotify() {
        try {
            log.info("Manual SSL certificate expiry check requested");
            
            notificationService.manuallyCheckAndNotify();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "SSL certificate expiry check completed successfully");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error during manual SSL certificate expiry check: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to complete SSL certificate expiry check");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/expiring-count")
    public ResponseEntity<Map<String, Object>> getExpiringCertificatesCount() {
        try {
            long count = notificationService.getCountOfCertificatesExpiringInTwoMonths();
            
            Map<String, Object> response = new HashMap<>();
            response.put("expiringCertificatesCount", count);
            response.put("message", "Retrieved count of certificates expiring in 2 months");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting expiring certificates count: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get expiring certificates count");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("serviceName", "SSL Certificate Notification Service");
            status.put("status", "ACTIVE");
            status.put("scheduledTime", "Every day at 9:00 AM");
            status.put("notificationWindow", "2 months before expiry");
            status.put("recipients", "Cyber Security Specialists and System Admins");
            status.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(status);
            
        } catch (Exception e) {
            log.error("Error getting system status: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get system status");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
















