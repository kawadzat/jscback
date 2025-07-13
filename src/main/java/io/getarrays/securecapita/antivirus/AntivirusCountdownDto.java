package io.getarrays.securecapita.antivirus;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class AntivirusCountdownDto {
    
    private Long antivirusId;
    private String antivirusName;
    private String antivirusKey;
    private String vendor;
    private String version;
    private AntivirusStatus status;
    private Boolean isInstalled;
    
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
    
    // Dates
    private LocalDateTime expirationDate;
    private LocalDateTime currentDate;
    
    // Laptop information (if needed)
    private Long laptopId;
    private String laptopSerialNumber;
    private String laptopUser;
} 