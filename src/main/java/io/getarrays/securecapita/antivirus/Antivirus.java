package io.getarrays.securecapita.antivirus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.itinventory.Laptop;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name ="antivirus")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Antivirus extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "`key`", nullable = false, unique = true)
    private String key;

    @Column(name = "renew_time_interval")
    private Integer renewTimeInterval;

    @Column(name = "version")
    private String version;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AntivirusStatus status;

    @Column(name = "is_installed")
    private Boolean isInstalled;

    @Column(name = "license_expiration_date")
    private LocalDateTime licenseExpirationDate;

    @Column(name = "days_to_expiration")
    private Long daysToExpiration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @JsonBackReference
    private Laptop laptop;

    public long getDaysToExpiration() {
        if (licenseExpirationDate == null) return -1;
        return java.time.Duration.between(
            java.time.LocalDateTime.now(),
            licenseExpirationDate
        ).toDays();
    }

} 