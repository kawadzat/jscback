package io.getarrays.securecapita.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatusIn(List<TaskStatusEnum> statuses, Pageable pageable);

    @Query("SELECT t FROM Task t")
    Page<Task> filterTasks(Pageable pageable);

    @Query("SELECT t FROM Task t WHERE " +
            "(:ownedByMe IS NULL OR (t.initiatedUser.id = :currentUserId AND :ownedByMe = TRUE)) AND " +
            "(:assignedToMe IS NULL OR (:currentUserId IN (SELECT u.id FROM t.assignedUsers u) AND :assignedToMe = " +
            "TRUE)) AND " +
            "(:stationId IS NULL OR EXISTS (" +
            "   SELECT us FROM UserStation us WHERE " +
            "   (us.user.id IN (SELECT u.id FROM t.assignedUsers u)) " +
            "   AND us.station.id = :stationId" +
            ")) AND " +
            "(:departmentId IS NULL OR EXISTS (" +
            "   SELECT ud FROM UserDepartment ud WHERE " +
            "   (ud.user.id IN (SELECT u.id FROM t.assignedUsers u)) " +
            "   AND ud.department.id = :departmentId" +
            "))")
    Page<Task> findTasksByFilters(
            @Param("currentUserId") Long currentUserId,
            @Param("ownedByMe") Boolean ownedByMe,
            @Param("assignedToMe") Boolean assignedToMe,
            @Param("stationId") Long stationId,
            @Param("departmentId") Long departmentId,
            Pageable pageable
    );


    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE :userId IN (SELECT u.id FROM t.assignedUsers u) " +
            "AND t.status = 'PENDING'")
    int countPendingTasksForUser(@Param("userId") Long userId);


    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE :userId IN (SELECT u.id FROM t.assignedUsers u) " +
            "AND t.status = 'COMPLETED'")
    int countCompletedTasksForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE :userId IN (SELECT u.id FROM t.assignedUsers u)")
    int countTotalAssignedTasksForUser(@Param("userId") Long userId);

    @Query("""
    SELECT t.status, COUNT(DISTINCT t)
    FROM Task t
    JOIN t.assignedUsers u
    JOIN u.stations us
    JOIN u.userDepartments ud
    WHERE t.initiatedDate BETWEEN :startDate AND :endDate
    AND (:userId IS NULL OR u.id = :userId)
    AND (:stationId IS NULL OR us.station.station_id = :stationId)
    AND (:departmentId IS NULL OR ud.department.id = :departmentId)
    GROUP BY t.status
""")
    List<Object[]> countTasksByStatusWithFilters(@Param("startDate") Date startDate,
                                                 @Param("endDate") Date endDate,
                                                 @Param("userId") Long userId,
                                                 @Param("stationId") Long stationId,
                                                 @Param("departmentId") Long departmentId);

    @Query("""
    SELECT u, t.status, COUNT(DISTINCT t)
    FROM Task t
    JOIN t.assignedUsers u
    JOIN u.stations us
    JOIN u.userDepartments ud
    WHERE t.initiatedDate BETWEEN :startDate AND :endDate
    AND (:userId IS NULL OR u.id = :userId)
    AND (:stationId IS NULL OR us.station.station_id = :stationId)
    AND (:departmentId IS NULL OR ud.department.id = :departmentId)
    GROUP BY u.id, t.status
    ORDER BY u.firstName, u.lastName
""")
    List<Object[]> countTasksByUserAndStatusWithFilters(@Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        @Param("userId") Long userId,
                                                        @Param("stationId") Long stationId,
                                                        @Param("departmentId") Long departmentId);

    // Repetitive task reminder queries
    @Query("SELECT t FROM Task t WHERE t.type = 'REPETITIVE' AND t.status IN ('PENDING', 'IN_PROGRESS')")
    List<Task> findRepetitiveTasksNeedingReminders();

    @Query("SELECT t FROM Task t WHERE t.type = 'REPETITIVE' AND t.nextRepetitionDate <= :reminderDate AND t.status IN ('PENDING', 'IN_PROGRESS')")
    List<Task> findRepetitiveTasksDueForReminders(@Param("reminderDate") Date reminderDate);

    @Query("SELECT t FROM Task t WHERE t.type = 'REPETITIVE' AND t.nextRepetitionDate IS NOT NULL AND t.nextRepetitionDate <= :dueDate")
    List<Task> findRepetitiveTasksWithUpcomingDueDate(@Param("dueDate") Date dueDate);

    @Query("SELECT t FROM Task t WHERE t.type = 'REPETITIVE' AND t.status = 'COMPLETED' AND t.nextRepetitionDate <= :currentDate")
    List<Task> findCompletedRepetitiveTasksReadyForNextCycle(@Param("currentDate") Date currentDate);

}
