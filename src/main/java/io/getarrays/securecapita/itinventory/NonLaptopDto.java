package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * Data Transfer Object for Non-Laptop IT Assets
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonLaptopDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Issue date is required")
    private Date issueDate;

    private Date replacementDate;

    @NotNull(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Asset type is required")
    @Size(max = 50, message = "Asset type must not exceed 50 characters")
    private String assetType;

    @Size(max = 100, message = "Model number must not exceed 100 characters")
    private String modelNumber;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    @NotNull(message = "Purchase date is required")
    private Date purchaseDate;

    private Double purchasePrice;

    private String location;

    private String status;

    private UserDTO issuedByUser;

    private Set<UserDTO> assignedUsers;

    private Date createdAt;

    private Date updatedAt;

    private String notes;
} 