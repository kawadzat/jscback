package io.getarrays.securecapita.asserts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssertsResponseDto {
    private int totalAsserts;

    private List<StationAssertsDto> stationWiseAsserts;
}
