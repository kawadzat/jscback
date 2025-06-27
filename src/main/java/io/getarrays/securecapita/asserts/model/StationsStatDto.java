package io.getarrays.securecapita.asserts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationsStatDto {
    private String name;
    private Long assets;
    private Long officesCount;
    private List<StationsAssetsStatDto> assetsList;

    public StationsStatDto(String name,Long assets,Long officesCount){
        this.name=name;
        this.assets=assets;
        this.officesCount=officesCount;
    }
}

