package io.getarrays.securecapita.itinventory.SSLCERTIFICATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "sslcertificate")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class SslCerticate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date purchaseDate;

    @Column(name = "plan", length = 100)
    private String plan;

    @Column(name = "validity", length = 100)
    private String validity;

    @Column(name = "vendor", length = 100)
    private String vendor;

    @Column(name = "supplier", length = 100)
    private String supplier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date expiryDate;

    @Column(name = "domain_name", length = 255)
    private String domainName;

    @Column(name = "certificate_serial_number", length = 255)
    private String certificateSerialNumber;

    @Column(name = "notes", length = 1000)
    private String notes;

}
