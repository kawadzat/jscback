package io.getarrays.securecapita.recordings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Recordings entity.
 * Defines business logic methods for court recordings management.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
public interface RecordingsService {
    
    // Basic CRUD operations
    Recordings create(Recordings recording);
    Recordings update(Recordings recording);
    Recordings getById(Long id);
    List<Recordings> getAll();
    void delete(Long id);
    
    // Duplicate prevention
    boolean isFileNameExists(String fileName);
    Recordings createWithDuplicateCheck(Recordings recording);
    Recordings updateWithDuplicateCheck(Long id, Recordings recording);
    
    // Search and filter methods
    List<Recordings> getByStatus(RecordingStatus status);
    Page<Recordings> getByStatus(RecordingStatus status, Pageable pageable);
    
    List<Recordings> getByJudge(String judge);
    Page<Recordings> getByJudge(String judge, Pageable pageable);
    
    List<Recordings> getByCaseDetails(String caseDetails);
    Page<Recordings> getByCaseDetails(String caseDetails, Pageable pageable);
    
    List<Recordings> getByCaseType(String caseType);
    Page<Recordings> getByCaseType(String caseType, Pageable pageable);
    
    List<Recordings> getByCourtRoom(String courtRoom);
    Page<Recordings> getByCourtRoom(String courtRoom, Pageable pageable);
    
    List<Recordings> getByDateRange(Date startDate, Date endDate);
    Page<Recordings> getByDateRange(Date startDate, Date endDate, Pageable pageable);
    
    List<Recordings> getByDurationRange(Double minDuration, Double maxDuration);
    Page<Recordings> getByDurationRange(Double minDuration, Double maxDuration, Pageable pageable);
    
    List<Recordings> getByFileSizeRange(Double minSize, Double maxSize);
    Page<Recordings> getByFileSizeRange(Double minSize, Double maxSize, Pageable pageable);
    
    List<Recordings> getArchivedRecordings();
    Page<Recordings> getArchivedRecordings(Pageable pageable);
    
    List<Recordings> getNonArchivedRecordings();
    Page<Recordings> getNonArchivedRecordings(Pageable pageable);
    
    // Advanced search
    Page<Recordings> searchRecordings(String caseDetails, String judge, String caseType, 
                                     RecordingStatus status, Boolean isArchived, Pageable pageable);
    
    // Business logic methods
    Recordings markAsBacked(Long id);
    Recordings markAsProcessing(Long id);
    Recordings markAsFailed(Long id);
    Recordings archiveRecording(Long id);
    Recordings unarchiveRecording(Long id);
    Recordings updateBackupDate(Long id, Date backupDate);
    
    // File management
    Recordings updateFilePath(Long id, String filePath);
    Recordings updateFileName(Long id, String fileName);
    Recordings updateFileSize(Long id, Double fileSizeMb);
    
    // Status management
    Recordings changeStatus(Long id, RecordingStatus newStatus);
    List<Recordings> getRecordingsNeedingBackup();
    
    // Reporting methods
    Map<RecordingStatus, Long> getRecordingCountByStatus();
    Map<String, Long> getRecordingCountByJudge();
    Map<String, Long> getRecordingCountByCaseType();
    
    Long getTotalRecordingCount();
    Long getRecordingsCountByStatus(RecordingStatus status);
    Long getArchivedRecordingsCount();
    Long getNonArchivedRecordingsCount();
    
    Double getTotalDuration();
    Double getTotalFileSize();
    Double getAverageDuration();
    Double getAverageFileSize();
    
    // Time-based queries
    List<Recordings> getRecordingsCreatedToday();
    List<Recordings> getRecordingsCreatedThisWeek();
    List<Recordings> getRecordingsCreatedThisMonth();
    List<Recordings> getRecordingsCreatedBetween(Date startDate, Date endDate);
    
    // Statistics
    Map<String, Object> getRecordingStatistics();
    Map<String, Object> getRecordingStatisticsByJudge(String judge);
    Map<String, Object> getRecordingStatisticsByCaseType(String caseType);
    
    // Bulk operations
    List<Recordings> bulkUpdateStatus(List<Long> ids, RecordingStatus status);
    List<Recordings> bulkArchive(List<Long> ids);
    List<Recordings> bulkUnarchive(List<Long> ids);
    void bulkDelete(List<Long> ids);
}



