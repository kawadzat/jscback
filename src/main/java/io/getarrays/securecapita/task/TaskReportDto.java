package io.getarrays.securecapita.task;

import lombok.Data;

@Data
public class TaskReportDto {
    Long pendingCount;

    Long inProgressCount;

    Long completedCount;
}
