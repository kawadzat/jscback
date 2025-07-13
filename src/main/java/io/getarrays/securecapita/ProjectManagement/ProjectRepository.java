package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find by project code
    Optional<Project> findByCode(String code);
    boolean existsByCode(String code);

    // Find by status
    List<Project> findByStatus(ProjectStatus status);
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    // Find by priority
    List<Project> findByPriority(ProjectPriority priority);
    Page<Project> findByPriority(ProjectPriority priority, Pageable pageable);

    // Find by type
    List<Project> findByType(ProjectType type);
    Page<Project> findByType(ProjectType type, Pageable pageable);

    // Find by project manager
    List<Project> findByProjectManager(User projectManager);
    Page<Project> findByProjectManager(User projectManager, Pageable pageable);

    // Find by client
    List<Project> findByClient(User client);
    Page<Project> findByClient(User client, Pageable pageable);

    // Find by team member
    @Query("SELECT p FROM Project p JOIN p.teamMembers tm WHERE tm = :teamMember")
    List<Project> findByTeamMember(@Param("teamMember") User teamMember);

    @Query("SELECT p FROM Project p JOIN p.teamMembers tm WHERE tm = :teamMember")
    Page<Project> findByTeamMember(@Param("teamMember") User teamMember, Pageable pageable);

    // Find by department
    List<Project> findByDepartment(String department);
    Page<Project> findByDepartment(String department, Pageable pageable);

    // Find by location
    List<Project> findByLocation(String location);
    Page<Project> findByLocation(String location, Pageable pageable);

    // Find active projects
    List<Project> findByIsActiveTrue();
    Page<Project> findByIsActiveTrue(Pageable pageable);

    // Find template projects
    List<Project> findByIsTemplateTrue();
    Page<Project> findByIsTemplateTrue(Pageable pageable);

    // Find by parent project
    List<Project> findByParentProjectId(Long parentProjectId);

    // Find by risk level
    List<Project> findByRiskLevel(RiskLevel riskLevel);
    Page<Project> findByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    // Find projects by date range
    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Project> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    // Find overdue projects
    @Query("SELECT p FROM Project p WHERE p.endDate < :currentDate AND p.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Project> findOverdueProjects(@Param("currentDate") LocalDate currentDate);

    // Find projects ending soon
    @Query("SELECT p FROM Project p WHERE p.endDate BETWEEN :startDate AND :endDate AND p.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Project> findProjectsEndingSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find projects by progress percentage
    List<Project> findByProgressPercentage(Integer progressPercentage);
    List<Project> findByProgressPercentageLessThan(Integer progressPercentage);
    List<Project> findByProgressPercentageGreaterThan(Integer progressPercentage);

    // Find projects by budget range
    @Query("SELECT p FROM Project p WHERE p.budget BETWEEN :minBudget AND :maxBudget")
    List<Project> findByBudgetRange(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget);

    // Find projects by name containing
    List<Project> findByNameContainingIgnoreCase(String name);
    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find projects by description containing
    List<Project> findByDescriptionContainingIgnoreCase(String description);
    Page<Project> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    // Find projects by tags containing
    List<Project> findByTagsContainingIgnoreCase(String tags);
    Page<Project> findByTagsContainingIgnoreCase(String tags, Pageable pageable);

    // Count projects by status
    long countByStatus(ProjectStatus status);

    // Count projects by priority
    long countByPriority(ProjectPriority priority);

    // Count projects by type
    long countByType(ProjectType type);

    // Count active projects
    long countByIsActiveTrue();

    // Count overdue projects
    @Query("SELECT COUNT(p) FROM Project p WHERE p.endDate < :currentDate AND p.status NOT IN ('COMPLETED', 'CANCELLED')")
    long countOverdueProjects(@Param("currentDate") LocalDate currentDate);

    // Find projects with high risk
    @Query("SELECT p FROM Project p WHERE p.riskLevel = 'HIGH' OR p.riskLevel = 'CRITICAL'")
    List<Project> findHighRiskProjects();

    // Find projects with low progress
    @Query("SELECT p FROM Project p WHERE p.progressPercentage < 50 AND p.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Project> findLowProgressProjects();

    // Find projects by multiple criteria
    @Query("SELECT p FROM Project p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:priority IS NULL OR p.priority = :priority) AND " +
           "(:type IS NULL OR p.type = :type) AND " +
           "(:department IS NULL OR p.department = :department) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Project> findByMultipleCriteria(
            @Param("status") ProjectStatus status,
            @Param("priority") ProjectPriority priority,
            @Param("type") ProjectType type,
            @Param("department") String department,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
} 