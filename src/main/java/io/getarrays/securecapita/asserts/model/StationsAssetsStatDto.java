package io.getarrays.securecapita.asserts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationsAssetsStatDto {
    private String name;
    private String station;
    private Long assets;
}

