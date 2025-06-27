package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.getarrays.securecapita.maintenance.MaintenanceDto;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenanceStatus;
import io.getarrays.securecapita.maintenance.MaintenanceType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for Laptop entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaptopDto {
    private List<MaintenanceDto> maintenanceList;
    private Long id;
    @NotNull(message = "Purchase date is required")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Serial number is required")
    private String serialNumber;


    @NotNull(message = "RAM is required")
    @Min(value = 1, message = "RAM must be at least 1 GB")
    private Integer ram;

    @NotNull(message = "Processor is required")
    @Min(value = 1, message = "Processor value must be positive")
    private Integer processor;

    @NotNull(message = "Issue date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @NotNull(message = "Status is required")
    private LaptopStatus status;

    @NotBlank(message = "IssuedTo is required")
    private String issuedTo;


    @NotBlank(message = "department is required")
    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "department", length = 100, nullable = false)
    private String department;


    @NotBlank(message = "designation is required")
    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "designation", length = 100, nullable = false)
    private String designation;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Replacement date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replacementDate;

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
