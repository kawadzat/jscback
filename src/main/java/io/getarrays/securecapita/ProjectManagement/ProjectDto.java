package io.getarrays.securecapita.ProjectManagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {
    
    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Project code is required")
    @Size(max = 50, message = "Project code must not exceed 50 characters")
    private String code;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualEndDate;

    @NotNull(message = "Project status is required")
    private String status;

    @NotNull(message = "Project priority is required")
    private String priority;

    @NotNull(message = "Project type is required")
    private String type;

    private BigDecimal budget;
    private BigDecimal actualCost;
    private Integer progressPercentage;

    private Long projectManagerId;
    private UserDTO projectManager;

    private Long clientId;
    private UserDTO client;

    private Set<Long> teamMemberIds = new HashSet<>();
    private Set<UserDTO> teamMembers = new HashSet<>();

    private String location;
    private String department;
    private String tags;

    private String riskLevel;
    private String riskDescription;
    private Integer qualityScore;
    private Integer customerSatisfactionScore;

    private Boolean isActive = true;
    private Boolean isTemplate = false;
    private Long parentProjectId;
    private String notes;
    private String completionCriteria;
    private String successMetrics;

    // Computed fields
    private Long daysRemaining;
    private Boolean isOverdue;
    private Boolean isOnTrack;
    private BigDecimal budgetUtilization;
    private String statusColor;
    private String priorityColor;

    // Audit fields
    private String createdBy;
    private LocalDate createdAt;
    private String lastModifiedBy;
    private LocalDate lastModifiedAt;
} 