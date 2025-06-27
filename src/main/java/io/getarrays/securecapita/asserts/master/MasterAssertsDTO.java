package io.getarrays.securecapita.asserts.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterAssertsDTO {
    private String name;
    private Long currentYearTotal;
    private Long previousYearTotal;
    private String remark;

    private int requiredLevel;
}
