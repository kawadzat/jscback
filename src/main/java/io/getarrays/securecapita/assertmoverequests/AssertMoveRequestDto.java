package io.getarrays.securecapita.assertmoverequests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssertMoveRequestDto {
    @NotNull
    private Long assertId;
    @NotNull
    private Long locationId;

    @NotNull
    private String reason;
}
