package io.getarrays.securecapita.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSearchDto {
    private int page = 0;

    private int size = 10;

    private Boolean ownedByMe;

    private Boolean assignedToMe;

    private Long stationId;

    private Long departmentId;
}
