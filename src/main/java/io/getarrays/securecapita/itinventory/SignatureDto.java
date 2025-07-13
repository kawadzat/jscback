package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignatureDto {
    private Long id;
    private String signatureData; // Base64-encoded signature data
    private String signatureHash; // SHA-256 hash for verification
    private LocalDateTime signatureTimestamp;
    private LocalDateTime expirationTimestamp;
    private String ipAddress;
    private String userAgent;
    private String certificateInfo;
    private String signatureAlgorithm;
    private Integer keySize;
    private Boolean isValid;
    private Integer validationAttempts;
    private LocalDateTime lastValidated;
    private String validationStatus;
    private String revocationReason;
    private LocalDateTime revocationTimestamp;
    private UserDTO signedBy;
    private Long laptopAcknowledgmentId;
    private Long laptopId;
    private String signaturePurpose;
    private String signatureMetadata;
    private Boolean isArchived;
    private LocalDateTime archiveTimestamp;
    private String archiveReason;
    private String signatureVersion;
    private String complianceLevel;
    private String auditTrail;
    
    // Computed fields
    private Boolean isExpired;
    private Boolean isRevoked;
    private Boolean isCurrentlyValid;
    private Long signatureAgeInDays;
    private Long daysUntilExpiration;
    
    /**
     * Convert entity to DTO
     */
    public static SignatureDto fromEntity(Signature signature) {
        if (signature == null) {
            return null;
        }
        
        return SignatureDto.builder()
                .id(signature.getId())
                .signatureData(signature.getSignatureData())
                .signatureHash(signature.getSignatureHash())
                .signatureTimestamp(signature.getSignatureTimestamp())
                .expirationTimestamp(signature.getExpirationTimestamp())
                .ipAddress(signature.getIpAddress())
                .userAgent(signature.getUserAgent())
                .certificateInfo(signature.getCertificateInfo())
                .signatureAlgorithm(signature.getSignatureAlgorithm())
                .keySize(signature.getKeySize())
                .isValid(signature.getIsValid())
                .validationAttempts(signature.getValidationAttempts())
                .lastValidated(signature.getLastValidated())
                .validationStatus(signature.getValidationStatus())
                .revocationReason(signature.getRevocationReason())
                .revocationTimestamp(signature.getRevocationTimestamp())
                .signedBy(signature.getSignedBy() != null ? UserDTO.toDto(signature.getSignedBy()) : null)
                .laptopAcknowledgmentId(signature.getLaptopAcknowledgment() != null ? signature.getLaptopAcknowledgment().getId() : null)
                .laptopId(signature.getLaptopId())
                .signaturePurpose(signature.getSignaturePurpose())
                .signatureMetadata(signature.getSignatureMetadata())
                .isArchived(signature.getIsArchived())
                .archiveTimestamp(signature.getArchiveTimestamp())
                .archiveReason(signature.getArchiveReason())
                .signatureVersion(signature.getSignatureVersion())
                .complianceLevel(signature.getComplianceLevel())
                .auditTrail(signature.getAuditTrail())
                // Computed fields
                .isExpired(signature.isExpired())
                .isRevoked(signature.isRevoked())
                .isCurrentlyValid(signature.isCurrentlyValid())
                .signatureAgeInDays(signature.getSignatureAgeInDays())
                .daysUntilExpiration(signature.getDaysUntilExpiration())
                .build();
    }
    
    /**
     * Convert DTO to entity (for creation/updates)
     */
    public Signature toEntity() {
        return Signature.builder()
                .id(this.id)
                .signatureData(this.signatureData)
                .signatureHash(this.signatureHash)
                .signatureTimestamp(this.signatureTimestamp)
                .expirationTimestamp(this.expirationTimestamp)
                .ipAddress(this.ipAddress)
                .userAgent(this.userAgent)
                .certificateInfo(this.certificateInfo)
                .signatureAlgorithm(this.signatureAlgorithm)
                .keySize(this.keySize)
                .isValid(this.isValid)
                .validationAttempts(this.validationAttempts)
                .lastValidated(this.lastValidated)
                .validationStatus(this.validationStatus)
                .revocationReason(this.revocationReason)
                .revocationTimestamp(this.revocationTimestamp)
                .laptopId(this.laptopId)
                .signaturePurpose(this.signaturePurpose)
                .signatureMetadata(this.signatureMetadata)
                .isArchived(this.isArchived)
                .archiveTimestamp(this.archiveTimestamp)
                .archiveReason(this.archiveReason)
                .signatureVersion(this.signatureVersion)
                .complianceLevel(this.complianceLevel)
                .auditTrail(this.auditTrail)
                .build();
    }
    
    /**
     * Create a minimal DTO for public consumption (without sensitive data)
     */
    public static SignatureDto createPublicDto(Signature signature) {
        if (signature == null) {
            return null;
        }
        
        return SignatureDto.builder()
                .id(signature.getId())
                .signatureHash(signature.getSignatureHash())
                .signatureTimestamp(signature.getSignatureTimestamp())
                .expirationTimestamp(signature.getExpirationTimestamp())
                .isValid(signature.getIsValid())
                .validationStatus(signature.getValidationStatus())
                .laptopId(signature.getLaptopId())
                .signaturePurpose(signature.getSignaturePurpose())
                .isArchived(signature.getIsArchived())
                .signatureVersion(signature.getSignatureVersion())
                .complianceLevel(signature.getComplianceLevel())
                // Computed fields
                .isExpired(signature.isExpired())
                .isRevoked(signature.isRevoked())
                .isCurrentlyValid(signature.isCurrentlyValid())
                .signatureAgeInDays(signature.getSignatureAgeInDays())
                .daysUntilExpiration(signature.getDaysUntilExpiration())
                .build();
    }
} 