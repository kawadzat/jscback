package io.getarrays.securecapita.antivirus;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "antivirus")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
@Data
public class AntivirusDto {

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

    // Countdown information
    private Boolean hasExpirationDate;
    private Boolean isExpired;
    private String urgencyLevel;
    private String formattedCountdown;
    private String message;
    
    // Time breakdown
    private Long days;
    private Long hours;
    private Long minutes;
    private Long seconds;
    private Long totalHours;
    private Long totalMinutes;
    private Long totalSeconds;
    
    // For expired licenses
    private Long expiredDays;
    
    // Current date for reference
    private LocalDateTime currentDate;

}
