package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for representing laptops with their assigned licenses
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaptopWithLicencesDto {
    
    private Long laptopId;
    private String laptopSerialNumber;
    private String laptopManufacturer;
    private LaptopStatus laptopStatus;
    private List<LicenceDto> assignedLicences;
    private Integer licenceCount;
} 