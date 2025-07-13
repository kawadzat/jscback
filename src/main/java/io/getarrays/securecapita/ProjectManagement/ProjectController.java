package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<CustomMessage> createProject(@AuthenticationPrincipal UserDTO currentUser,
                                                      @RequestBody @Valid ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProject(currentUser, projectDto);
        return ResponseEntity.ok(new CustomMessage("Project created successfully", createdProject));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<CustomMessage> updateProject(@AuthenticationPrincipal UserDTO currentUser,
                                                      @PathVariable Long projectId,
                                                      @RequestBody @Valid ProjectDto projectDto) {
        ProjectDto updatedProject = projectService.updateProject(currentUser, projectId, projectDto);
        return ResponseEntity.ok(new CustomMessage("Project updated successfully", updatedProject));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) {
        ProjectDto project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<CustomMessage> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(new CustomMessage("Project deleted successfully", null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getProjectsByStatus(@PathVariable ProjectStatus status,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getProjectsByStatus(status, page, size));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<?> getProjectsByPriority(@PathVariable ProjectPriority priority,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getProjectsByPriority(priority, page, size));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getProjectsByType(@PathVariable ProjectType type,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getProjectsByType(type, page, size));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getProjectsByManager(@PathVariable Long managerId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getProjectsByManager(managerId, page, size));
    }

    @GetMapping("/team-member/{memberId}")
    public ResponseEntity<?> getProjectsByTeamMember(@PathVariable Long memberId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getProjectsByTeamMember(memberId, page, size));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ProjectDto>> getOverdueProjects() {
        List<ProjectDto> overdueProjects = projectService.getOverdueProjects();
        return ResponseEntity.ok(overdueProjects);
    }

    @GetMapping("/high-risk")
    public ResponseEntity<List<ProjectDto>> getHighRiskProjects() {
        List<ProjectDto> highRiskProjects = projectService.getHighRiskProjects();
        return ResponseEntity.ok(highRiskProjects);
    }

    @GetMapping("/low-progress")
    public ResponseEntity<List<ProjectDto>> getLowProgressProjects() {
        List<ProjectDto> lowProgressProjects = projectService.getLowProgressProjects();
        return ResponseEntity.ok(lowProgressProjects);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProjects(@RequestParam String searchTerm,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.searchProjects(searchTerm, page, size));
    }

    @PutMapping("/{projectId}/progress")
    public ResponseEntity<CustomMessage> updateProjectProgress(@PathVariable Long projectId,
                                                              @RequestParam Integer progressPercentage) {
        ProjectDto updatedProject = projectService.updateProjectProgress(projectId, progressPercentage);
        return ResponseEntity.ok(new CustomMessage("Project progress updated successfully", updatedProject));
    }

    @PutMapping("/{projectId}/status")
    public ResponseEntity<CustomMessage> updateProjectStatus(@PathVariable Long projectId,
                                                            @RequestParam ProjectStatus status) {
        ProjectDto updatedProject = projectService.updateProjectStatus(projectId, status);
        return ResponseEntity.ok(new CustomMessage("Project status updated successfully", updatedProject));
    }

    @PostMapping("/{projectId}/team-members/{userId}")
    public ResponseEntity<CustomMessage> addTeamMember(@PathVariable Long projectId,
                                                      @PathVariable Long userId) {
        ProjectDto updatedProject = projectService.addTeamMember(projectId, userId);
        return ResponseEntity.ok(new CustomMessage("Team member added successfully", updatedProject));
    }

    @DeleteMapping("/{projectId}/team-members/{userId}")
    public ResponseEntity<CustomMessage> removeTeamMember(@PathVariable Long projectId,
                                                         @PathVariable Long userId) {
        ProjectDto updatedProject = projectService.removeTeamMember(projectId, userId);
        return ResponseEntity.ok(new CustomMessage("Team member removed successfully", updatedProject));
    }

    @PutMapping("/{projectId}/budget")
    public ResponseEntity<CustomMessage> updateProjectBudget(@PathVariable Long projectId,
                                                            @RequestParam java.math.BigDecimal budget) {
        ProjectDto updatedProject = projectService.updateProjectBudget(projectId, budget);
        return ResponseEntity.ok(new CustomMessage("Project budget updated successfully", updatedProject));
    }

    @PutMapping("/{projectId}/actual-cost")
    public ResponseEntity<CustomMessage> updateActualCost(@PathVariable Long projectId,
                                                         @RequestParam java.math.BigDecimal actualCost) {
        ProjectDto updatedProject = projectService.updateActualCost(projectId, actualCost);
        return ResponseEntity.ok(new CustomMessage("Actual cost updated successfully", updatedProject));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getProjectStats() {
        // This would typically return aggregated statistics
        // For now, returning a simple message
        return ResponseEntity.ok(new CustomMessage("Project statistics", "Dashboard stats endpoint"));
    }

    @GetMapping("/dashboard/recent")
    public ResponseEntity<?> getRecentProjects(@RequestParam(defaultValue = "5") int limit) {
        // This would return recent projects for dashboard
        return ResponseEntity.ok(projectService.getAllProjects(0, limit));
    }
} 