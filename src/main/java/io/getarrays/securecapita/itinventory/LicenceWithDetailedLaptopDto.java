package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class LicenceWithDetailedLaptopDto {


    private Long id;
    private String assetType;
    private String licenseName;
    private String description;
    private String licenseKey;
    private Date purchaseDate;
    private Date installationDate;
    private Date expiryDate;
    private LicenseType licenseType;
    private LicenseStatus status;
    private String supplier;
    private String vendor;
    private Integer numberOfSeats;
    private Double price;
    private String currency;
    private String version;
    private String installationPath;
    private String filePath;
    private Long daysToExpiration;
    private String department;
    private String station;
    private String notes;






    // Laptop Information
    private Long laptopId;
    private String laptopSerialNumber;
    private String laptopManufacturer;
    private String laptopAssertType;









}
