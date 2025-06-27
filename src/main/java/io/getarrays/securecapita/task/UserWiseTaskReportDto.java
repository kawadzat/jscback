package io.getarrays.securecapita.task;

import io.getarrays.securecapita.dto.UserDTO;
import lombok.Data;

@Data
public class UserWiseTaskReportDto {
    UserDTO user;

    TaskReportDto taskStats;
}
