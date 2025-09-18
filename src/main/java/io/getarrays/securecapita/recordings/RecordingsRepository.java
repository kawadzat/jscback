package io.getarrays.securecapita.recordings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Recordings entity.
 * Provides data access methods for court recordings.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Repository
public interface RecordingsRepository extends JpaRepository<Recordings, Long> {
    
    // Find by status
    List<Recordings> findByStatus(RecordingStatus status);
    Page<Recordings> findByStatus(RecordingStatus status, Pageable pageable);
    
    // Find by judge
    List<Recordings> findByJudge(String judge);
    Page<Recordings> findByJudge(String judge, Pageable pageable);
    
    // Find by case details
    List<Recordings> findByCaseDetailsContainingIgnoreCase(String caseDetails);
    Page<Recordings> findByCaseDetailsContainingIgnoreCase(String caseDetails, Pageable pageable);
    
    // Find by case type
    List<Recordings> findByCaseType(String caseType);
    Page<Recordings> findByCaseType(String caseType, Pageable pageable);
    
    // Find by court room
    List<Recordings> findByCourtRoom(String courtRoom);
    Page<Recordings> findByCourtRoom(String courtRoom, Pageable pageable);
    
    // Find by recording date range
    List<Recordings> findByRecordingDateTimeBetween(Date startDate, Date endDate);
    Page<Recordings> findByRecordingDateTimeBetween(Date startDate, Date endDate, Pageable pageable);
    
    // Find by duration range
    @Query("SELECT r FROM Recordings r WHERE r.durationMinutes BETWEEN :minDuration AND :maxDuration")
    List<Recordings> findByDurationRange(@Param("minDuration") Double minDuration, @Param("maxDuration") Double maxDuration);
    
    @Query("SELECT r FROM Recordings r WHERE r.durationMinutes BETWEEN :minDuration AND :maxDuration")
    Page<Recordings> findByDurationRange(@Param("minDuration") Double minDuration, @Param("maxDuration") Double maxDuration, Pageable pageable);
    
    // Find by file size range
    @Query("SELECT r FROM Recordings r WHERE r.fileSizeMb BETWEEN :minSize AND :maxSize")
    List<Recordings> findByFileSizeRange(@Param("minSize") Double minSize, @Param("maxSize") Double maxSize);
    
    @Query("SELECT r FROM Recordings r WHERE r.fileSizeMb BETWEEN :minSize AND :maxSize")
    Page<Recordings> findByFileSizeRange(@Param("minSize") Double minSize, @Param("maxSize") Double maxSize, Pageable pageable);
    
    // Find archived recordings
    List<Recordings> findByIsArchived(Boolean isArchived);
    Page<Recordings> findByIsArchived(Boolean isArchived, Pageable pageable);
    
    // Find recordings that need backup
    List<Recordings> findByStatusAndBackupDateIsNull(RecordingStatus status);
    
    // Find recordings by file name
    Optional<Recordings> findByFileName(String fileName);
    boolean existsByFileName(String fileName);
    
    // Find recordings by file path
    List<Recordings> findByFilePathContaining(String filePath);
    
    // Search recordings by multiple criteria
    @Query("SELECT r FROM Recordings r WHERE " +
           "(:caseDetails IS NULL OR LOWER(r.caseDetails) LIKE LOWER(CONCAT('%', :caseDetails, '%'))) AND " +
           "(:judge IS NULL OR LOWER(r.judge) LIKE LOWER(CONCAT('%', :judge, '%'))) AND " +
           "(:caseType IS NULL OR LOWER(r.caseType) LIKE LOWER(CONCAT('%', :caseType, '%'))) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:isArchived IS NULL OR r.isArchived = :isArchived)")
    Page<Recordings> searchRecordings(@Param("caseDetails") String caseDetails,
                                    @Param("judge") String judge,
                                    @Param("caseType") String caseType,
                                    @Param("status") RecordingStatus status,
                                    @Param("isArchived") Boolean isArchived,
                                    Pageable pageable);
    
    // Count recordings by status
    @Query("SELECT r.status, COUNT(r) FROM Recordings r GROUP BY r.status")
    List<Object[]> countRecordingsByStatus();
    
    // Count recordings by judge
    @Query("SELECT r.judge, COUNT(r) FROM Recordings r WHERE r.judge IS NOT NULL GROUP BY r.judge")
    List<Object[]> countRecordingsByJudge();
    
    // Count recordings by case type
    @Query("SELECT r.caseType, COUNT(r) FROM Recordings r WHERE r.caseType IS NOT NULL GROUP BY r.caseType")
    List<Object[]> countRecordingsByCaseType();
    
    // Get total duration of recordings
    @Query("SELECT SUM(r.durationMinutes) FROM Recordings r WHERE r.durationMinutes IS NOT NULL")
    Double getTotalDuration();
    
    // Get total file size of recordings
    @Query("SELECT SUM(r.fileSizeMb) FROM Recordings r WHERE r.fileSizeMb IS NOT NULL")
    Double getTotalFileSize();
    
    // Find recordings created today
    @Query("SELECT r FROM Recordings r WHERE DATE(r.recordingDateTime) = CURRENT_DATE")
    List<Recordings> findRecordingsCreatedToday();
    
    // Find recordings created this week
    @Query("SELECT r FROM Recordings r WHERE r.recordingDateTime >= :weekStart")
    List<Recordings> findRecordingsCreatedThisWeek(@Param("weekStart") Date weekStart);
    
    // Find recordings created this month
    @Query("SELECT r FROM Recordings r WHERE YEAR(r.recordingDateTime) = YEAR(CURRENT_DATE) AND MONTH(r.recordingDateTime) = MONTH(CURRENT_DATE)")
    List<Recordings> findRecordingsCreatedThisMonth();
}



