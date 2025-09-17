package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String issueType;
    private Long assigneeId;
    private UserDTO assignee;
    private Long reporterId;
    private UserDTO reporter;
    private Long projectId;
    private String projectName;
    private String tags;
    private LocalDateTime dueDate;
    private LocalDateTime resolvedDate;
    private LocalDateTime acknowledgedDate;
    private String notes;
    private Double estimatedHours;
    private Double actualHours;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}



