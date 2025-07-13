package io.getarrays.securecapita.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * DTO for repetitive task reminder information
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepetitiveTaskReminderDto {
    
    private Long taskId;
    private String taskCode;
    private String taskTitle;
    private String taskStatus;
    private Date dueDate;
    private Date nextRepetitionDate;
    private Integer timeIntervalValue;
    private String timeIntervalUnit;
    private List<String> assignedUserEmails;
    private List<String> assignedUserNames;
    private Date lastReminderSent;
    private boolean reminderSent;
    private String reminderMessage;
    
    /**
     * Create DTO from Task entity
     */
    public static RepetitiveTaskReminderDto fromTask(Task task) {
        return RepetitiveTaskReminderDto.builder()
            .taskId(task.getId())
            .taskCode(task.getCode())
            .taskTitle(task.getTitle())
            .taskStatus(task.getStatus().name())
            .dueDate(task.getDueDate())
            .nextRepetitionDate(task.getNextRepetitionDate())
            .timeIntervalValue(task.getTimeIntervalValue())
            .timeIntervalUnit(task.getTimeIntervalUnit() != null ? task.getTimeIntervalUnit().getDisplayName() : null)
            .assignedUserEmails(task.getAssignedUsers().stream()
                .map(user -> user.getEmail())
                .toList())
            .assignedUserNames(task.getAssignedUsers().stream()
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .toList())
            .build();
    }
} 