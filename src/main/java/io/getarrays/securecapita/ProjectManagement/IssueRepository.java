package io.getarrays.securecapita.ProjectManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByAssigneeId(Long assigneeId);

    List<Issue> findByReporterId(Long reporterId);

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByPriority(IssuePriority priority);

    List<Issue> findByIssueType(IssueType issueType);

    @Query("SELECT i FROM Issue i WHERE i.status = :status AND i.assignee.id = :assigneeId")
    List<Issue> findByStatusAndAssigneeId(@Param("status") IssueStatus status, @Param("assigneeId") Long assigneeId);

    @Query("SELECT i FROM Issue i WHERE i.status = 'PENDING_ACKNOWLEDGMENT'")
    List<Issue> findPendingAcknowledgmentIssues();

    @Query("SELECT i FROM Issue i WHERE i.status = 'PENDING_ACKNOWLEDGMENT' AND i.assignee.id = :assigneeId")
    List<Issue> findPendingAcknowledgmentIssuesByAssignee(@Param("assigneeId") Long assigneeId);

    Optional<Issue> findByIdAndStatus(Long id, IssueStatus status);
}



