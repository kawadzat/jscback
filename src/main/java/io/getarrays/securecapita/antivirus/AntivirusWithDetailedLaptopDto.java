package io.getarrays.securecapita.antivirus;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class AntivirusWithDetailedLaptopDto {

    // Antivirus Information
    private Long antivirusId;
    private String antivirusName;
    private String antivirusKey;
    private Integer renewTimeInterval;
    private String version;
    private String vendor;
    private AntivirusStatus status;
    private Boolean isInstalled;
    private LocalDateTime licenseExpirationDate;
    private LocalDateTime lastScanDate;
    private Long daysToExpiration;

    // Laptop Information
    private Long laptopId;
    private String laptopSerialNumber;
    private String laptopManufacturer;
    private String laptopAssertType;
    private Integer laptopRam;
    private Integer laptopProcessor;
    private Date laptopPurchaseDate;
    private Date laptopIssueDate;
    private String laptopStatus;
    private String laptopIssuedTo;
    private String laptopStation;
    private String laptopDepartment;
    private String laptopDesignation;
    private String laptopEmail;
    private Date laptopReplacementDate;
    private String laptopIssueByEmail;

    // Additional computed fields
    private Boolean isLicenseExpired;
    private String licenseStatus;
    private Long daysUntilExpiration;
    private String urgencyLevel;
    private String formattedExpirationDate;
    private String formattedLastScanDate;
} 