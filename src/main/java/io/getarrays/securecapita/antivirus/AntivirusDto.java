package io.getarrays.securecapita.antivirus;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class AntivirusDto {

    private Long id;
    private String name;
    private String key;
    private Integer renewTimeInterval;
    private String version;
    private String vendor;
    private AntivirusStatus status;
    private Boolean isInstalled;
    private LocalDateTime licenseExpirationDate;
    private LocalDateTime lastScanDate;
    private Long daysToExpiration;

    // Laptop information
    private String laptopSerialNumber;
    private String laptopUser;
    private Long laptopId;

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
