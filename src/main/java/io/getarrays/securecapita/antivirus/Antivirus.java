package io.getarrays.securecapita.antivirus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itinventory.Laptop;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor

@Table(name = "`antivirus`")
public class Antivirus {
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

    @Column(name = "last_scan_date")
    private LocalDateTime lastScanDate;

    @Column(name = "days_to_expiration")
    private Long daysToExpiration;

    // Relationship with Laptop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @JsonIgnore
    private Laptop laptop;

}
