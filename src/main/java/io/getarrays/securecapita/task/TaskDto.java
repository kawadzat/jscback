package io.getarrays.securecapita.task;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.utils.TodayOrFuture;
import io.getarrays.securecapita.utils.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class TaskDto {
    private Long id;

    private String code;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Date inititatedDate;

    @NotNull(message = "startDate is required")
    @TodayOrFuture(message = "startDate must be in the present or future")
    private Date startDate;

    @NotNull(message = "dueDate is required")
    @FutureOrPresent(message = "Due date must be in the future")
    private Date dueDate;

    @NotNull(message = "priority is required")
    @ValidEnum(enumClass = PriorityEnum.class, message = "Invalid priority value")
    private String priority;

    @NotNull(message = "status is required")
    @ValidEnum(enumClass = TaskStatusEnum.class, message = "Invalid status value")
    private String status;

    private UserDTO initiatedUser;

    @NotEmpty(message = "assignedUserIds must not be empty.")
    private Set<Long> assignedUserIds;

    private Set<UserDTO> assignedUsers;
}
