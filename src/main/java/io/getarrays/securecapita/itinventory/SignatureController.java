package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/signatures")
@AllArgsConstructor
public class SignatureController {
    
    private final SignatureService signatureService;
    
    /**
     * Generate a digital signature for laptop acknowledgment
     */
    @PostMapping("/generate")
    public ResponseEntity<CustomMessage> generateSignature(@AuthenticationPrincipal UserDTO currentUser,
                                                         @RequestBody @Valid LaptopAcknowledgmentDto acknowledgmentDto) {
        String signature = signatureService.generateSignature(acknowledgmentDto, currentUser);
        return ResponseEntity.ok(new CustomMessage("Signature generated successfully", signature));
    }
    
    /**
     * Validate a digital signature
     */
    @PostMapping("/validate")
    public ResponseEntity<CustomMessage> validateSignature(@AuthenticationPrincipal UserDTO currentUser,
                                                         @RequestBody SignatureValidationRequest request) {
        boolean isValid = signatureService.validateSignature(request.getSignature(), request.getAcknowledgmentDto(), currentUser);
        return ResponseEntity.ok(new CustomMessage("Signature validation completed", isValid));
    }
    
    /**
     * Verify signature authenticity and integrity
     */
    @PostMapping("/verify/{laptopId}")
    public ResponseEntity<CustomMessage> verifySignature(@PathVariable Long laptopId,
                                                       @RequestBody SignatureVerificationRequest request) {
        SignatureVerificationResult result = signatureService.verifySignature(laptopId, request.getSignature());
        return ResponseEntity.ok(new CustomMessage("Signature verification completed", result));
    }
    
    /**
     * Generate signature hash for storage
     */
    @PostMapping("/hash")
    public ResponseEntity<CustomMessage> generateSignatureHash(@RequestBody SignatureHashRequest request) {
        String hash = signatureService.generateSignatureHash(request.getSignature(), request.getLaptopId(), request.getUserId());
        return ResponseEntity.ok(new CustomMessage("Signature hash generated successfully", hash));
    }
    
    /**
     * Get signature statistics for current user
     */
    @GetMapping("/statistics")
    public ResponseEntity<CustomMessage> getSignatureStatistics(@AuthenticationPrincipal UserDTO currentUser) {
        SignatureStatistics statistics = signatureService.getSignatureStatistics(currentUser);
        return ResponseEntity.ok(new CustomMessage("Signature statistics retrieved successfully", statistics));
    }
    
    /**
     * Get signature metadata for a laptop
     */
    @GetMapping("/metadata/{laptopId}")
    public ResponseEntity<CustomMessage> getSignatureMetadata(@PathVariable Long laptopId) {
        SignatureVerificationResult result = signatureService.verifySignature(laptopId, null);
        return ResponseEntity.ok(new CustomMessage("Signature metadata retrieved successfully", result));
    }
    
    // Request DTOs for the controller
    public static class SignatureValidationRequest {
        private String signature;
        private LaptopAcknowledgmentDto acknowledgmentDto;
        
        // Getters and setters
        public String getSignature() { return signature; }
        public void setSignature(String signature) { this.signature = signature; }
        public LaptopAcknowledgmentDto getAcknowledgmentDto() { return acknowledgmentDto; }
        public void setAcknowledgmentDto(LaptopAcknowledgmentDto acknowledgmentDto) { this.acknowledgmentDto = acknowledgmentDto; }
    }
    
    public static class SignatureVerificationRequest {
        private String signature;
        
        // Getters and setters
        public String getSignature() { return signature; }
        public void setSignature(String signature) { this.signature = signature; }
    }
    
    public static class SignatureHashRequest {
        private String signature;
        private Long laptopId;
        private Long userId;
        
        // Getters and setters
        public String getSignature() { return signature; }
        public void setSignature(String signature) { this.signature = signature; }
        public Long getLaptopId() { return laptopId; }
        public void setLaptopId(Long laptopId) { this.laptopId = laptopId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
} 