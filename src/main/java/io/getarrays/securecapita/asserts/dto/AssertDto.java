package io.getarrays.securecapita.asserts.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssertDto {
    @NotNull(message = "assetNumber must not be null")
    private String assetNumber;

    @NotNull(message = "location must not be null")
    private String location;

    @NotNull(message = "selectedStationID must not be null")
    private Long selectedStationID;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "date must not be null")
    private Timestamp date;

    @NotNull(message = "assetDisc must not be null")
    private String assetDisc;

    @NotNull(message = "movable must not be null")
    private Boolean movable;

    @NotNull(message = "assertType must not be null")
    private String assertType;

    @NotNull(message = "initialRemarks must not be null")
    private String initialRemarks;

    @NotNull(message = "serialNumber must not be null")
    private String serialNumber;

    @NotNull(message = "invoiceNumber must not be null")
    private String invoiceNumber;

    @NotNull(message = "quantity must not be null")
    private int quantity;
}
