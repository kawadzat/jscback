package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import io.getarrays.securecapita.antivirus.AntivirusDto;
import io.getarrays.securecapita.maintenance.MaintenanceDto;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenanceStatus;
import io.getarrays.securecapita.maintenance.MaintenanceType;
import io.getarrays.securecapita.itinventory.validation.LaptopConditionalValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import io.getarrays.securecapita.dto.UserDTO;

/**
 * Data Transfer Object for Laptop entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@LaptopConditionalValidation
public class LaptopDto {
    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<MaintenanceDto> maintenanceList = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
   private List<AntivirusDto> antivirusList = new ArrayList<>();
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    
    private String manufacturer;

    private String assertType;

    private String serialNumber;

    // Laptop-specific fields - only required if asset type is LAPTOP
    @Min(value = 1, message = "RAM must be at least 1 GB")
    private Integer ram;

    @Min(value = 1, message = "Processor value must be positive")
    private Integer processor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    private LaptopStatus status;

    private String issuedTo;

    @Size(max = 100, message = "station name must not exceed 100 characters")
    private String station;

    @Size(max = 100, message = "department name must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "department name must not exceed 100 characters")
    private String designation;
    
    @Email(message = "Email should be valid")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replacementDate;

    private String issueByEmail;

    private String notes;

    private UserDTO acknowledgedBy;
    private LocalDate acknowledgmentDate;
    private String signature;

    // Custom validation method
    public boolean isLaptopSpecificFieldsRequired() {
        return "LAPTOP".equalsIgnoreCase(assertType);
    }

    public void setMaintenanceList(List<MaintenanceDto> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    private MaintenanceDto maintenanceEntityToDto(Maintenance maintenance) {
        MaintenanceDto dto = new MaintenanceDto();
        dto.setId(maintenance.getId());
        dto.setDescription(maintenance.getDescription());
        dto.setMaintenanceType(MaintenanceType.valueOf(maintenance.getMaintenanceType().toString()));
        dto.setStatus(MaintenanceStatus.valueOf(maintenance.getStatus().toString()));
        dto.setScheduledDate(maintenance.getScheduledDate());
        // ... add other fields as needed ...
        return dto;
    }
}
