package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.codegenerator.CodeGeneratorService;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.dtomapper.UserDTOMapper;
import io.getarrays.securecapita.exception.BadRequestException;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository1 userRepository1;
    private final CodeGeneratorService codeGeneratorService;

    public ProjectDto getProjectById(Long projectId) {
        return entityToDto(findProjectByIdOrThrow(projectId));
    }

    public PageResponseDto<ProjectDto> getAllProjects(int page, int size) {
        Page<Project> projectPage = projectRepository.findAll(
            PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public ProjectDto createProject(UserDTO currentUser, ProjectDto projectDto) {
        validateProjectDto(projectDto);
        Project project = dtoToEntity(currentUser, null, projectDto);
        
        // Generate project code if not provided
        if (project.getCode() == null || project.getCode().trim().isEmpty()) {
            project.setCode(generateProjectCode());
        }
        
        // Set default values
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.PLANNING);
        }
        if (project.getPriority() == null) {
            project.setPriority(ProjectPriority.MEDIUM);
        }
        if (project.getType() == null) {
            project.setType(ProjectType.OTHER);
        }
        if (project.getProgressPercentage() == null) {
            project.setProgressPercentage(0);
        }
        if (project.getIsActive() == null) {
            project.setIsActive(true);
        }
        
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto updateProject(UserDTO currentUser, Long projectId, ProjectDto projectDto) {
        validateProjectDto(projectDto);
        Project project = dtoToEntity(currentUser, projectId, projectDto);
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public void deleteProject(Long projectId) {
        Project project = findProjectByIdOrThrow(projectId);
        projectRepository.delete(project);
    }

    public PageResponseDto<ProjectDto> getProjectsByStatus(ProjectStatus status, int page, int size) {
        Page<Project> projectPage = projectRepository.findByStatus(
            status, PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public PageResponseDto<ProjectDto> getProjectsByPriority(ProjectPriority priority, int page, int size) {
        Page<Project> projectPage = projectRepository.findByPriority(
            priority, PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public PageResponseDto<ProjectDto> getProjectsByType(ProjectType type, int page, int size) {
        Page<Project> projectPage = projectRepository.findByType(
            type, PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public PageResponseDto<ProjectDto> getProjectsByManager(Long managerId, int page, int size) {
        User manager = userRepository1.findById(managerId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + managerId));
        
        Page<Project> projectPage = projectRepository.findByProjectManager(
            manager, PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public PageResponseDto<ProjectDto> getProjectsByTeamMember(Long memberId, int page, int size) {
        User member = userRepository1.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + memberId));
        
        Page<Project> projectPage = projectRepository.findByTeamMember(
            member, PageRequest.of(page, size, Sort.by("lastModifiedDate").descending())
        );
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public List<ProjectDto> getOverdueProjects() {
        List<Project> overdueProjects = projectRepository.findOverdueProjects(LocalDate.now());
        return overdueProjects.stream().map(this::entityToDto).toList();
    }

    public List<ProjectDto> getHighRiskProjects() {
        List<Project> highRiskProjects = projectRepository.findHighRiskProjects();
        return highRiskProjects.stream().map(this::entityToDto).toList();
    }

    public List<ProjectDto> getLowProgressProjects() {
        List<Project> lowProgressProjects = projectRepository.findLowProgressProjects();
        return lowProgressProjects.stream().map(this::entityToDto).toList();
    }

    public PageResponseDto<ProjectDto> searchProjects(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate").descending());
        Page<Project> projectPage = projectRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        
        if (projectPage.isEmpty()) {
            projectPage = projectRepository.findByDescriptionContainingIgnoreCase(searchTerm, pageable);
        }
        
        return new PageResponseDto<>(
            projectPage.getContent().stream().map(this::entityToDto).toList(),
            projectPage
        );
    }

    public ProjectDto updateProjectProgress(Long projectId, Integer progressPercentage) {
        Project project = findProjectByIdOrThrow(projectId);
        project.setProgressPercentage(progressPercentage);
        
        // Update status based on progress
        if (progressPercentage >= 100) {
            project.setStatus(ProjectStatus.COMPLETED);
            project.setActualEndDate(LocalDate.now());
        } else if (progressPercentage > 0 && project.getStatus() == ProjectStatus.PLANNING) {
            project.setStatus(ProjectStatus.ACTIVE);
            if (project.getActualStartDate() == null) {
                project.setActualStartDate(LocalDate.now());
            }
        }
        
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto updateProjectStatus(Long projectId, ProjectStatus status) {
        Project project = findProjectByIdOrThrow(projectId);
        project.setStatus(status);
        
        // Update dates based on status
        if (status == ProjectStatus.COMPLETED && project.getActualEndDate() == null) {
            project.setActualEndDate(LocalDate.now());
        } else if (status == ProjectStatus.ACTIVE && project.getActualStartDate() == null) {
            project.setActualStartDate(LocalDate.now());
        }
        
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto addTeamMember(Long projectId, Long userId) {
        Project project = findProjectByIdOrThrow(projectId);
        User user = userRepository1.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        project.getTeamMembers().add(user);
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto removeTeamMember(Long projectId, Long userId) {
        Project project = findProjectByIdOrThrow(projectId);
        User user = userRepository1.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        project.getTeamMembers().remove(user);
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto updateProjectBudget(Long projectId, BigDecimal budget) {
        Project project = findProjectByIdOrThrow(projectId);
        project.setBudget(budget);
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    public ProjectDto updateActualCost(Long projectId, BigDecimal actualCost) {
        Project project = findProjectByIdOrThrow(projectId);
        project.setActualCost(actualCost);
        Project savedProject = projectRepository.save(project);
        return entityToDto(savedProject);
    }

    // Helper methods
    private Project findProjectByIdOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    private void validateProjectDto(ProjectDto projectDto) {
        if (projectDto.getStartDate() != null && projectDto.getEndDate() != null) {
            if (projectDto.getStartDate().isAfter(projectDto.getEndDate())) {
                throw new BadRequestException("Start date cannot be after end date");
            }
        }
        
        if (projectDto.getProgressPercentage() != null) {
            if (projectDto.getProgressPercentage() < 0 || projectDto.getProgressPercentage() > 100) {
                throw new BadRequestException("Progress percentage must be between 0 and 100");
            }
        }
        
        if (projectDto.getBudget() != null && projectDto.getBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Budget cannot be negative");
        }
        
        if (projectDto.getActualCost() != null && projectDto.getActualCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Actual cost cannot be negative");
        }
    }

    private String generateProjectCode() {
        String prefix = "PRJ-";
        int code = codeGeneratorService.generateCode("Project");
        return prefix + code;
    }

    private Project dtoToEntity(UserDTO currentUser, Long projectId, ProjectDto projectDto) {
        Project project;
        
        if (projectId != null) {
            project = findProjectByIdOrThrow(projectId);
        } else {
            project = new Project();
        }
        
        // Map basic fields
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setCode(projectDto.getCode());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setActualStartDate(projectDto.getActualStartDate());
        project.setActualEndDate(projectDto.getActualEndDate());
        project.setBudget(projectDto.getBudget());
        project.setActualCost(projectDto.getActualCost());
        project.setProgressPercentage(projectDto.getProgressPercentage());
        project.setLocation(projectDto.getLocation());
        project.setDepartment(projectDto.getDepartment());
        project.setTags(projectDto.getTags());
        project.setRiskDescription(projectDto.getRiskDescription());
        project.setQualityScore(projectDto.getQualityScore());
        project.setCustomerSatisfactionScore(projectDto.getCustomerSatisfactionScore());
        project.setIsActive(projectDto.getIsActive());
        project.setIsTemplate(projectDto.getIsTemplate());
        project.setParentProjectId(projectDto.getParentProjectId());
        project.setNotes(projectDto.getNotes());
        project.setCompletionCriteria(projectDto.getCompletionCriteria());
        project.setSuccessMetrics(projectDto.getSuccessMetrics());
        
        // Map enums
        if (projectDto.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(projectDto.getStatus()));
        }
        if (projectDto.getPriority() != null) {
            project.setPriority(ProjectPriority.valueOf(projectDto.getPriority()));
        }
        if (projectDto.getType() != null) {
            project.setType(ProjectType.valueOf(projectDto.getType()));
        }
        if (projectDto.getRiskLevel() != null) {
            project.setRiskLevel(RiskLevel.valueOf(projectDto.getRiskLevel()));
        }
        
        // Map relationships
        if (projectDto.getProjectManagerId() != null) {
            User projectManager = userRepository1.findById(projectDto.getProjectManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Project manager not found"));
            project.setProjectManager(projectManager);
        }
        
        if (projectDto.getClientId() != null) {
            User client = userRepository1.findById(projectDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
            project.setClient(client);
        }
        
        // Map team members
        if (projectDto.getTeamMemberIds() != null && !projectDto.getTeamMemberIds().isEmpty()) {
            Set<User> teamMembers = projectDto.getTeamMemberIds().stream()
                .map(userId -> userRepository1.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + userId)))
                .collect(Collectors.toSet());
            project.setTeamMembers(teamMembers);
        }
        
        return project;
    }

    private ProjectDto entityToDto(Project project) {
        ProjectDto dto = ProjectDto.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .code(project.getCode())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .budget(project.getBudget())
            .actualCost(project.getActualCost())
            .progressPercentage(project.getProgressPercentage())
            .location(project.getLocation())
            .department(project.getDepartment())
            .tags(project.getTags())
            .riskDescription(project.getRiskDescription())
            .qualityScore(project.getQualityScore())
            .customerSatisfactionScore(project.getCustomerSatisfactionScore())
            .isActive(project.getIsActive())
            .isTemplate(project.getIsTemplate())
            .parentProjectId(project.getParentProjectId())
            .notes(project.getNotes())
            .completionCriteria(project.getCompletionCriteria())
            .successMetrics(project.getSuccessMetrics())
            .build();
        
        // Map enums to strings
        if (project.getStatus() != null) {
            dto.setStatus(project.getStatus().name());
        }
        if (project.getPriority() != null) {
            dto.setPriority(project.getPriority().name());
        }
        if (project.getType() != null) {
            dto.setType(project.getType().name());
        }
        if (project.getRiskLevel() != null) {
            dto.setRiskLevel(project.getRiskLevel().name());
        }
        
        // Map relationships
        if (project.getProjectManager() != null) {
            dto.setProjectManagerId(project.getProjectManager().getId());
            dto.setProjectManager(UserDTOMapper.fromUser(project.getProjectManager()));
        }
        
        if (project.getClient() != null) {
            dto.setClientId(project.getClient().getId());
            dto.setClient(UserDTOMapper.fromUser(project.getClient()));
        }
        
        // Map team members
        if (project.getTeamMembers() != null && !project.getTeamMembers().isEmpty()) {
            Set<Long> teamMemberIds = project.getTeamMembers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
            dto.setTeamMemberIds(teamMemberIds);
            
            Set<UserDTO> teamMemberDtos = project.getTeamMembers().stream()
                .map(UserDTOMapper::fromUser)
                .collect(Collectors.toSet());
            dto.setTeamMembers(teamMemberDtos);
        }
        
        // Calculate computed fields
        calculateComputedFields(project, dto);
        
        return dto;
    }

    private void calculateComputedFields(Project project, ProjectDto dto) {
        LocalDate today = LocalDate.now();
        
        // Calculate days remaining
        if (project.getEndDate() != null) {
            dto.setDaysRemaining(ChronoUnit.DAYS.between(today, project.getEndDate()));
        }
        
        // Calculate if overdue
        if (project.getEndDate() != null && project.getStatus() != ProjectStatus.COMPLETED && project.getStatus() != ProjectStatus.CANCELLED) {
            dto.setIsOverdue(project.getEndDate().isBefore(today));
        }
        
        // Calculate if on track
        if (project.getStartDate() != null && project.getEndDate() != null && project.getProgressPercentage() != null) {
            long totalDays = ChronoUnit.DAYS.between(project.getStartDate(), project.getEndDate());
            long elapsedDays = ChronoUnit.DAYS.between(project.getStartDate(), today);
            
            if (totalDays > 0 && elapsedDays > 0) {
                double expectedProgress = (double) elapsedDays / totalDays * 100;
                dto.setIsOnTrack(project.getProgressPercentage() >= expectedProgress);
            }
        }
        
        // Calculate budget utilization
        if (project.getBudget() != null && project.getActualCost() != null && project.getBudget().compareTo(BigDecimal.ZERO) > 0) {
            dto.setBudgetUtilization(project.getActualCost().divide(project.getBudget(), 4, BigDecimal.ROUND_HALF_UP));
        }
        
        // Set status color
        if (project.getStatus() != null) {
            switch (project.getStatus()) {
                case COMPLETED -> dto.setStatusColor("#28a745");
                case ACTIVE -> dto.setStatusColor("#007bff");
                case PLANNING -> dto.setStatusColor("#ffc107");
                case ON_HOLD -> dto.setStatusColor("#fd7e14");
                case CANCELLED -> dto.setStatusColor("#dc3545");
                default -> dto.setStatusColor("#6c757d");
            }
        }
        
        // Set priority color
        if (project.getPriority() != null) {
            switch (project.getPriority()) {
                case CRITICAL -> dto.setPriorityColor("#dc3545");
                case URGENT -> dto.setPriorityColor("#fd7e14");
                case HIGH -> dto.setPriorityColor("#ffc107");
                case MEDIUM -> dto.setPriorityColor("#007bff");
                case LOW -> dto.setPriorityColor("#28a745");
                default -> dto.setPriorityColor("#6c757d");
            }
        }
    }
} 