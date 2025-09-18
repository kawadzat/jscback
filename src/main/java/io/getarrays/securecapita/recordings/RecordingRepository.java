package io.getarrays.securecapita.recordings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Recording entity operations.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Repository
public interface RecordingRepository extends JpaRepository<Recording, Long> {
    
    // Find recordings by status
    List<Recording> findByStatus(RecordingStatus status);
    
    // Find recordings by judge
    List<Recording> findByJudge(String judge);
    
    // Find recordings by case details (partial match)
    List<Recording> findByCaseDetailsContainingIgnoreCase(String caseDetails);
    
    // Find recordings within date range
    List<Recording> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find recordings by duration range
    List<Recording> findByDurationBetween(Integer minDuration, Integer maxDuration);
    
    // Find recordings that are not deleted
    @Query("SELECT r FROM Recording r WHERE r.status != 'DELETED'")
    List<Recording> findActiveRecordings();
    
    // Find recordings by multiple statuses
    @Query("SELECT r FROM Recording r WHERE r.status IN :statuses")
    List<Recording> findByStatusIn(@Param("statuses") List<RecordingStatus> statuses);
    
    // Count recordings by status
    @Query("SELECT COUNT(r) FROM Recording r WHERE r.status = :status")
    Long countByStatus(@Param("status") RecordingStatus status);
    
    // Find recordings with file information
    @Query("SELECT r FROM Recording r WHERE r.fileName IS NOT NULL AND r.filePath IS NOT NULL")
    List<Recording> findRecordingsWithFiles();
    
    // Find recordings without file information
    @Query("SELECT r FROM Recording r WHERE r.fileName IS NULL OR r.filePath IS NULL")
    List<Recording> findRecordingsWithoutFiles();
}
