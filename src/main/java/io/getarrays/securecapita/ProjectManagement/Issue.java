package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Issues")
@EqualsAndHashCode(callSuper = false)
public class Issue extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    private IssueStatus status = IssueStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private IssuePriority priority = IssuePriority.MEDIUM;

    @Column(name = "issue_type")
    @Enumerated(EnumType.STRING)
    private IssueType issueType = IssueType.BUG;

    private String tags;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    @Column(name = "acknowledged_date")
    private LocalDateTime acknowledgedDate;

    private String notes;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "actual_hours")
    private Double actualHours;

    // Getters and setters for enum fields
    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
        if (status == IssueStatus.ACKNOWLEDGED && acknowledgedDate == null) {
            this.acknowledgedDate = LocalDateTime.now();
        }
        if (status == IssueStatus.RESOLVED && resolvedDate == null) {
            this.resolvedDate = LocalDateTime.now();
        }
    }

}
