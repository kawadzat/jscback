package io.getarrays.securecapita.itinventory.IT.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class ITAssertDto {

    private Long id;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp date;

    @NotNull(message = "Asset description is required")
    private String assetDisc;

    @NotNull(message = "Asset number is required")
    private String assetNumber;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Movable status is required")
    private Boolean movable;

    @NotNull(message = "Serial number is required")
    private String serialNumber;

    @NotNull(message = "Invoice number is required")
    private String invoiceNumber;

    @NotNull(message = "Asset type is required")
    private String assertType;

    @NotNull(message = "Location is required")
    private String location;

    private Long officeLocationId;
    private OfficeLocation officeLocation;

    @NotNull(message = "Initial remarks are required")
    private String initialRemarks;

    private Long stationId;
    private Long selectedStationID;

    private Long preparedById;
    private User preparedBy;

    private Long checkedById;
    private User checkedBy;

    private Set<Long> assignedUserIds = new HashSet<>();
    private Set<User> assignedUsers = new HashSet<>();
} 