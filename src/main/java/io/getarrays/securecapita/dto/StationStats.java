package io.getarrays.securecapita.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 5/15/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationStats {
    private long totalStations;
    private List<StationItemStat> stationItemStats;
}
