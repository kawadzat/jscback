package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "signatures")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Signature extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signature_data", columnDefinition = "longtext", nullable = false)
    private String signatureData; // Base64-encoded signature data

    @Column(name = "signature_hash", length = 255, nullable = false, unique = true)
    private String signatureHash; // SHA-256 hash of the signature for verification

    @Column(name = "signature_timestamp", nullable = false)
    private LocalDateTime signatureTimestamp; // When the signature was created

    @Column(name = "expiration_timestamp")
    private LocalDateTime expirationTimestamp; // When the signature expires (optional)

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // IP address of the signer

    @Column(name = "user_agent", length = 500)
    private String userAgent; // Browser/device information

    @Column(name = "certificate_info", length = 1000)
    private String certificateInfo; // Digital certificate information if applicable

    @Column(name = "signature_algorithm", length = 100)
    private String signatureAlgorithm; // Algorithm used (SHA-256, RSA, etc.)

    @Column(name = "key_size")
    private Integer keySize; // Key size used for signature (if applicable)

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true; // Whether the signature is currently valid

    @Column(name = "validation_attempts")
    private Integer validationAttempts = 0; // Number of validation attempts

    @Column(name = "last_validated")
    private LocalDateTime lastValidated; // When the signature was last validated

    @Column(name = "validation_status", length = 50)
    private String validationStatus; // VALID, INVALID, EXPIRED, REVOKED, etc.

    @Column(name = "revocation_reason", length = 500)
    private String revocationReason; // Reason for revocation if applicable

    @Column(name = "revocation_timestamp")
    private LocalDateTime revocationTimestamp; // When the signature was revoked

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signed_by", nullable = false)
    private User signedBy; // User who created the signature

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_acknowledgment_id")
    private LaptopAcknowledgment laptopAcknowledgment; // Associated laptop acknowledgment

    @Column(name = "laptop_id")
    private Long laptopId; // Associated laptop ID for quick reference

    @Column(name = "signature_purpose", length = 100)
    private String signaturePurpose; // Purpose of the signature (ACKNOWLEDGMENT, APPROVAL, etc.)

    @Column(name = "signature_metadata", columnDefinition = "text")
    private String signatureMetadata; // JSON metadata about the signature

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false; // Whether the signature is archived

    @Column(name = "archive_timestamp")
    private LocalDateTime archiveTimestamp; // When the signature was archived

    @Column(name = "archive_reason", length = 500)
    private String archiveReason; // Reason for archiving

    @Column(name = "signature_version", length = 20)
    private String signatureVersion = "1.0"; // Version of the signature format

    @Column(name = "compliance_level", length = 50)
    private String complianceLevel; // Compliance level (BASIC, STANDARD, ENHANCED, etc.)

    @Column(name = "audit_trail", columnDefinition = "text")
    private String auditTrail; // JSON audit trail of signature operations

    @PrePersist
    protected void onCreate() {
        if (signatureTimestamp == null) {
            signatureTimestamp = LocalDateTime.now();
        }
        if (isValid == null) {
            isValid = true;
        }
        if (validationAttempts == null) {
            validationAttempts = 0;
        }
        if (isArchived == null) {
            isArchived = false;
        }
        if (signatureVersion == null) {
            signatureVersion = "1.0";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (lastValidated == null && validationStatus != null) {
            lastValidated = LocalDateTime.now();
        }
    }

    /**
     * Check if the signature is expired
     */
    public boolean isExpired() {
        return expirationTimestamp != null && LocalDateTime.now().isAfter(expirationTimestamp);
    }

    /**
     * Check if the signature is revoked
     */
    public boolean isRevoked() {
        return revocationTimestamp != null;
    }

    /**
     * Check if the signature is valid and not expired or revoked
     */
    public boolean isCurrentlyValid() {
        return isValid && !isExpired() && !isRevoked() && !isArchived;
    }

    /**
     * Increment validation attempts
     */
    public void incrementValidationAttempts() {
        this.validationAttempts++;
        this.lastValidated = LocalDateTime.now();
    }

    /**
     * Mark signature as invalid
     */
    public void markAsInvalid(String reason) {
        this.isValid = false;
        this.validationStatus = "INVALID";
        this.revocationReason = reason;
        this.revocationTimestamp = LocalDateTime.now();
    }

    /**
     * Archive the signature
     */
    public void archive(String reason) {
        this.isArchived = true;
        this.archiveReason = reason;
        this.archiveTimestamp = LocalDateTime.now();
    }

    /**
     * Get signature age in days
     */
    public long getSignatureAgeInDays() {
        return java.time.Duration.between(signatureTimestamp, LocalDateTime.now()).toDays();
    }

    /**
     * Get days until expiration
     */
    public Long getDaysUntilExpiration() {
        if (expirationTimestamp == null) {
            return null;
        }
        long days = java.time.Duration.between(LocalDateTime.now(), expirationTimestamp).toDays();
        return days > 0 ? days : 0L;
    }
}
