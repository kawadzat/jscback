package io.getarrays.securecapita.ProjectManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueAcknowledgmentRepository extends JpaRepository<IssueAcknowledgment, Long> {

    Optional<IssueAcknowledgment> findByIssueId(Long issueId);

    boolean existsByIssueId(Long issueId);
}



