package io.getarrays.securecapita.asserts.LongTermAsserts;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FixedAssertDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // Adjust pattern if needed
    private LocalDateTime acquisitionDate;

    @NotNull(message = "Asset Number must not be null")
    private String fixedAssetNumber;

    @NotNull(message = "Selected Station ID must not be null")
    private Long selectedStationID;

    @NotNull(message = "Asset Description must not be null")
    private String assetDisc;

    @NotNull(message = "Movable must not be null")
    private Boolean movable;

    @NotNull(message = "Asset Type must not be null")
    private String assertType;


}