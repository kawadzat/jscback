package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaptopAssignmentDto {
    
    @NotBlank(message = "IssuedTo is required")
    private String issuedTo;
    
    @NotBlank(message = "Station is required")
    @Size(max = 100, message = "Station name must not exceed 100 characters")
    private String station;
    
    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String department;
    
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
} 