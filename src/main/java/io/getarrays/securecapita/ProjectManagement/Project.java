package io.getarrays.securecapita.ProjectManagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Project code is required")
    @Size(max = 50, message = "Project code must not exceed 50 characters")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @NotNull(message = "Project status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @NotNull(message = "Project priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private ProjectPriority priority;

    @NotNull(message = "Project type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProjectType type;

    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "progress_percentage")
    private Integer progressPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "project_team_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> teamMembers = new HashSet<>();

    @Column(name = "location")
    private String location;

    @Column(name = "department")
    private String department;

    @Column(name = "tags")
    private String tags;

    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Column(name = "risk_description", columnDefinition = "TEXT")
    private String riskDescription;

    @Column(name = "quality_score")
    private Integer qualityScore;

    @Column(name = "customer_satisfaction_score")
    private Integer customerSatisfactionScore;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_template")
    private Boolean isTemplate = false;

    @Column(name = "parent_project_id")
    private Long parentProjectId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "completion_criteria", columnDefinition = "TEXT")
    private String completionCriteria;

    @Column(name = "success_metrics", columnDefinition = "TEXT")
    private String successMetrics;

    // Computed fields for business logic
    @Transient
    private Long daysRemaining;

    @Transient
    private Boolean isOverdue;

    @Transient
    private Boolean isOnTrack;

    @Transient
    private BigDecimal budgetUtilization;

    @Transient
    private String statusColor;

    @Transient
    private String priorityColor;
}
