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
@Table(name = "laptopAcknowledgment")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class LaptopAcknowledgment extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id", nullable = false)
    private Laptop laptop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acknowledged_by", nullable = false)
    private User acknowledgedBy;

    @Column(name = "acknowledgment_date", nullable = false)
    private LocalDateTime acknowledgmentDate;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "signature", columnDefinition = "longtext")
    private String signature; // Base64-encoded signature image
    
    @Column(name = "signature_type", length = 50)
    private String signatureType; // "DIGITAL_SIGNATURE", "DRAWN_SIGNATURE", "TYPED_SIGNATURE"
    
    @Column(name = "signature_timestamp")
    private LocalDateTime signatureTimestamp;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress; // IP address of the signer
    
    @Column(name = "user_agent", length = 500)
    private String userAgent; // Browser/device information
    
    @Column(name = "certificate_info", length = 1000)
    private String certificateInfo; // Digital certificate information if applicable
    
    @Column(name = "signature_hash", length = 255)
    private String signatureHash; // Hash of the signature for verification

    @PrePersist
    protected void onCreate() {
        if (acknowledgmentDate == null) {
            acknowledgmentDate = LocalDateTime.now();
        }
        if (signatureTimestamp == null) {
            signatureTimestamp = LocalDateTime.now();
        }
    }
}
