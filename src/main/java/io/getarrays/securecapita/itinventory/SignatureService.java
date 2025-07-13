package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class SignatureService {
    
    private final LaptopAcknowledgmentRepository laptopAcknowledgmentRepository;
    private final UserRepository1 userRepository1;
    
    /**
     * Generate a digital signature for laptop acknowledgment
     */
    public String generateSignature(LaptopAcknowledgmentDto acknowledgmentDto, UserDTO currentUser) {
        try {
            // Create signature payload
            String payload = createSignaturePayload(acknowledgmentDto, currentUser);
            
            // Generate hash of the payload
            String hash = generateHash(payload);
            
            // Add timestamp and user info to create final signature
            String finalSignature = hash + "|" + 
                                  LocalDateTime.now().toString() + "|" + 
                                  currentUser.getId() + "|" + 
                                  acknowledgmentDto.getLaptopId();
            
            return Base64.getEncoder().encodeToString(finalSignature.getBytes(StandardCharsets.UTF_8));
            
        } catch (Exception e) {
            log.error("Error generating signature for laptop acknowledgment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
    
    /**
     * Validate a digital signature
     */
    public boolean validateSignature(String signature, LaptopAcknowledgmentDto acknowledgmentDto, UserDTO currentUser) {
        try {
            // Decode the signature
            String decodedSignature = new String(Base64.getDecoder().decode(signature), StandardCharsets.UTF_8);
            
            // Split the signature components
            String[] parts = decodedSignature.split("\\|");
            if (parts.length != 4) {
                log.warn("Invalid signature format");
                return false;
            }
            
            String hash = parts[0];
            String timestamp = parts[1];
            String userId = parts[2];
            String laptopId = parts[3];
            
            // Verify user ID matches
            if (!userId.equals(currentUser.getId().toString())) {
                log.warn("Signature user ID mismatch");
                return false;
            }
            
            // Verify laptop ID matches
            if (!laptopId.equals(acknowledgmentDto.getLaptopId().toString())) {
                log.warn("Signature laptop ID mismatch");
                return false;
            }
            
            // Verify timestamp is recent (within 24 hours)
            LocalDateTime signatureTime = LocalDateTime.parse(timestamp);
            if (signatureTime.isBefore(LocalDateTime.now().minusHours(24))) {
                log.warn("Signature timestamp is too old");
                return false;
            }
            
            // Recreate payload and verify hash
            String payload = createSignaturePayload(acknowledgmentDto, currentUser);
            String expectedHash = generateHash(payload);
            
            return hash.equals(expectedHash);
            
        } catch (Exception e) {
            log.error("Error validating signature: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify signature authenticity and integrity
     */
    public SignatureVerificationResult verifySignature(Long laptopId, String signature) {
        try {
            // Get the acknowledgment
            Optional<LaptopAcknowledgment> acknowledgmentOpt = laptopAcknowledgmentRepository.findByLaptopId(laptopId);
            if (acknowledgmentOpt.isEmpty()) {
                return SignatureVerificationResult.builder()
                    .isValid(false)
                    .errorMessage("Acknowledgment not found")
                    .build();
            }
            
            LaptopAcknowledgment acknowledgment = acknowledgmentOpt.get();
            
            // Decode and parse signature
            String decodedSignature = new String(Base64.getDecoder().decode(signature), StandardCharsets.UTF_8);
            String[] parts = decodedSignature.split("\\|");
            
            if (parts.length != 4) {
                return SignatureVerificationResult.builder()
                    .isValid(false)
                    .errorMessage("Invalid signature format")
                    .build();
            }
            
            String hash = parts[0];
            String timestamp = parts[1];
            String userId = parts[2];
            String signatureLaptopId = parts[3];
            
            // Verify laptop ID
            if (!signatureLaptopId.equals(laptopId.toString())) {
                return SignatureVerificationResult.builder()
                    .isValid(false)
                    .errorMessage("Laptop ID mismatch")
                    .build();
            }
            
            // Get user information
            User user = userRepository1.findById(Long.parseLong(userId))
                .orElse(null);
            
            if (user == null) {
                return SignatureVerificationResult.builder()
                    .isValid(false)
                    .errorMessage("User not found")
                    .build();
            }
            
            // Create DTO for verification
            LaptopAcknowledgmentDto acknowledgmentDto = createAcknowledgmentDto(acknowledgment);
            UserDTO userDto = UserDTO.toDto(user);
            
            // Validate signature
            boolean isValid = validateSignature(signature, acknowledgmentDto, userDto);
            
            return SignatureVerificationResult.builder()
                .isValid(isValid)
                .signatureTimestamp(LocalDateTime.parse(timestamp))
                .signedBy(userDto)
                .laptopId(laptopId)
                .errorMessage(isValid ? null : "Signature validation failed")
                .build();
                
        } catch (Exception e) {
            log.error("Error verifying signature: {}", e.getMessage(), e);
            return SignatureVerificationResult.builder()
                .isValid(false)
                .errorMessage("Verification error: " + e.getMessage())
                .build();
        }
    }
    
    /**
     * Generate a unique signature hash for storage
     */
    public String generateSignatureHash(String signature, Long laptopId, Long userId) {
        try {
            String data = signature + "|" + laptopId + "|" + userId + "|" + System.currentTimeMillis();
            return generateHash(data);
        } catch (Exception e) {
            log.error("Error generating signature hash: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate signature hash", e);
        }
    }
    
    /**
     * Get signature statistics for a user
     */
    public SignatureStatistics getSignatureStatistics(UserDTO currentUser) {
        try {
            List<LaptopAcknowledgment> userAcknowledgment = laptopAcknowledgmentRepository
                .findByAcknowledgedByUserId(currentUser.getId());
            
            long totalSignatures = userAcknowledgment.size();
            long validSignatures = userAcknowledgment.stream()
                .filter(ack -> ack.getSignatureHash() != null)
                .count();
            
            return SignatureStatistics.builder()
                .totalSignatures(totalSignatures)
                .validSignatures(validSignatures)
                .invalidSignatures(totalSignatures - validSignatures)
                .lastSignatureDate(userAcknowledgment.stream()
                    .map(LaptopAcknowledgment::getSignatureTimestamp)
                    .max(LocalDateTime::compareTo)
                    .orElse(null))
                .build();
                
        } catch (Exception e) {
            log.error("Error getting signature statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get signature statistics", e);
        }
    }
    
    /**
     * Create signature payload for hashing
     */
    private String createSignaturePayload(LaptopAcknowledgmentDto acknowledgmentDto, UserDTO currentUser) {
        return String.format("%s|%s|%s|%s|%s|%s|%s",
            acknowledgmentDto.getLaptopId(),
            acknowledgmentDto.getLaptopSerialNumber(),
            acknowledgmentDto.getLaptopIssuedTo(),
            acknowledgmentDto.getLaptopStation(),
            currentUser.getId(),
            currentUser.getEmail(),
            acknowledgmentDto.getNotes() != null ? acknowledgmentDto.getNotes() : ""
        );
    }
    
    /**
     * Generate SHA-256 hash of the given string
     */
    private String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
    
    /**
     * Create acknowledgment DTO from entity
     */
    private LaptopAcknowledgmentDto createAcknowledgmentDto(LaptopAcknowledgment acknowledgment) {
        return LaptopAcknowledgmentDto.builder()
            .id(acknowledgment.getId())
            .laptopId(acknowledgment.getLaptop().getId())
            .acknowledgedBy(UserDTO.toDto(acknowledgment.getAcknowledgedBy()))
            .acknowledgmentDate(acknowledgment.getAcknowledgmentDate())
            .notes(acknowledgment.getNotes())
            .signature(acknowledgment.getSignature())
            .signatureType(acknowledgment.getSignatureType())
            .signatureTimestamp(acknowledgment.getSignatureTimestamp())
            .ipAddress(acknowledgment.getIpAddress())
            .userAgent(acknowledgment.getUserAgent())
            .certificateInfo(acknowledgment.getCertificateInfo())
            .signatureHash(acknowledgment.getSignatureHash())
            .laptopSerialNumber(acknowledgment.getLaptop().getSerialNumber())
            .laptopIssuedTo(acknowledgment.getLaptop().getIssuedTo())
            .laptopStation(acknowledgment.getLaptop().getStation())
            .build();
    }
}
