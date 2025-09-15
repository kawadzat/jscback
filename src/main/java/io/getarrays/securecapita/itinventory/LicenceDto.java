package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for Licence entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicenceDto {
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

    // Laptop information
    private Long laptopId;
    private String laptopSerialNumber;
    private String laptopManufacturer;
    private String laptopAssertType;
}