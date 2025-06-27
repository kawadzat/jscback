package io.getarrays.securecapita.dto;

import lombok.*;

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
public class AssetsStats {
    private long totalAsserts;
    private long totalFixedAsserts;
    private long totalCurrentAsserts;
    private long totalMovableAsserts;
    private List<AssetItemStat> assetsStats;
}
