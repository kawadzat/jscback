package io.getarrays.securecapita.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationItemStat {
    private Long stationId;
    private String name;
    private Long total;
    private AssetsStats assetsStats;

    public StationItemStat(Long stationId, String name,Long total){
        this.stationId=stationId;
        this.name=name;
        this.total=total;
    }
}
