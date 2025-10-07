package io.getarrays.securecapita.recordings;

import io.getarrays.securecapita.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Service implementation for Recordings entity.
 * Provides business logic for court recordings management.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecordingsServiceImpl implements RecordingsService {

    private final RecordingsRepository recordingsRepository;

    // Basic CRUD operations
    @Override
    public Recordings create(Recordings recording) {
        log.info("Creating new recording with case details: {}", recording.getCaseDetails());
        
        // Set default status if not provided
        if (recording.getStatus() == null) {
            recording.setStatus(RecordingStatus.RECORDING);
        }
        
        // Set default archived status if not provided
        if (recording.getIsArchived() == null) {
            recording.setIsArchived(false);
        }
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings update(Recordings recording) {
        log.info("Updating recording with id: {}", recording.getId());
        
        if (!recordingsRepository.existsById(recording.getId())) {
            throw new ResourceNotFoundException("Recording not found with id: " + recording.getId());
        }
        
        return recordingsRepository.save(recording);
    }

    @Override
    @Transactional(readOnly = true)
    public Recordings getById(Long id) {
        log.debug("Fetching recording with id: {}", id);
        
        return recordingsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recording not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getAll() {
        log.debug("Fetching all recordings");
        
        return recordingsRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting recording with id: {}", id);
        
        if (!recordingsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recording not found with id: " + id);
        }
        
        recordingsRepository.deleteById(id);
    }

    // Duplicate prevention
    @Override
    @Transactional(readOnly = true)
    public boolean isFileNameExists(String fileName) {
        return recordingsRepository.existsByFileName(fileName);
    }

    @Override
    public Recordings createWithDuplicateCheck(Recordings recording) {
        // Check for duplicate file name
        if (recording.getFileName() != null && recordingsRepository.existsByFileName(recording.getFileName())) {
            throw new IllegalArgumentException("Recording with file name '" + recording.getFileName() + "' already exists");
        }
        
        return create(recording);
    }

    @Override
    public Recordings updateWithDuplicateCheck(Long id, Recordings recording) {
        Recordings existingRecording = getById(id);
        
        // Check if new file name conflicts with another recording
        if (recording.getFileName() != null && 
            !Objects.equals(existingRecording.getFileName(), recording.getFileName()) && 
            recordingsRepository.existsByFileName(recording.getFileName())) {
            throw new IllegalArgumentException("Recording with file name '" + recording.getFileName() + "' already exists");
        }
        
        recording.setId(id);
        return update(recording);
    }

    // Search and filter methods
    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByStatus(RecordingStatus status) {
        return recordingsRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByStatus(RecordingStatus status, Pageable pageable) {
        return recordingsRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByJudge(String judge) {
        return recordingsRepository.findByJudge(judge);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByJudge(String judge, Pageable pageable) {
        return recordingsRepository.findByJudge(judge, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByCaseDetails(String caseDetails) {
        return recordingsRepository.findByCaseDetailsContainingIgnoreCase(caseDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByCaseDetails(String caseDetails, Pageable pageable) {
        return recordingsRepository.findByCaseDetailsContainingIgnoreCase(caseDetails, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByCaseType(String caseType) {
        return recordingsRepository.findByCaseType(caseType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByCaseType(String caseType, Pageable pageable) {
        return recordingsRepository.findByCaseType(caseType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByCourtRoom(String courtRoom) {
        return recordingsRepository.findByCourtRoom(courtRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByCourtRoom(String courtRoom, Pageable pageable) {
        return recordingsRepository.findByCourtRoom(courtRoom, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByDateRange(Date startDate, Date endDate) {
        return recordingsRepository.findByRecordingDateTimeBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByDateRange(Date startDate, Date endDate, Pageable pageable) {
        return recordingsRepository.findByRecordingDateTimeBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByDurationRange(Double minDuration, Double maxDuration) {
        return recordingsRepository.findByDurationRange(minDuration, maxDuration);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByDurationRange(Double minDuration, Double maxDuration, Pageable pageable) {
        return recordingsRepository.findByDurationRange(minDuration, maxDuration, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getByFileSizeRange(Double minSize, Double maxSize) {
        return recordingsRepository.findByFileSizeRange(minSize, maxSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getByFileSizeRange(Double minSize, Double maxSize, Pageable pageable) {
        return recordingsRepository.findByFileSizeRange(minSize, maxSize, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getArchivedRecordings() {
        return recordingsRepository.findByIsArchived(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getArchivedRecordings(Pageable pageable) {
        return recordingsRepository.findByIsArchived(true, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getNonArchivedRecordings() {
        return recordingsRepository.findByIsArchived(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> getNonArchivedRecordings(Pageable pageable) {
        return recordingsRepository.findByIsArchived(false, pageable);
    }

    // Advanced search
    @Override
    @Transactional(readOnly = true)
    public Page<Recordings> searchRecordings(String caseDetails, String judge, String caseType, 
                                           RecordingStatus status, Boolean isArchived, Pageable pageable) {
        return recordingsRepository.searchRecordings(caseDetails, judge, caseType, status, isArchived, pageable);
    }

    // Business logic methods
    @Override
    public Recordings markAsBacked(Long id) {
        log.info("Marking recording {} as backed", id);
        
        Recordings recording = getById(id);
        recording.setStatus(RecordingStatus.BACKED);
        recording.setBackupDate(new Date());
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings markAsProcessing(Long id) {
        log.info("Marking recording {} as recording", id);
        
        Recordings recording = getById(id);
        recording.setStatus(RecordingStatus.RECORDING);
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings markAsFailed(Long id) {
        log.info("Marking recording {} as deleted", id);
        
        Recordings recording = getById(id);
        recording.setStatus(RecordingStatus.DELETED);
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings archiveRecording(Long id) {
        log.info("Archiving recording {}", id);
        
        Recordings recording = getById(id);
        recording.setIsArchived(true);
        recording.setArchiveDate(new Date());
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings unarchiveRecording(Long id) {
        log.info("Unarchiving recording {}", id);
        
        Recordings recording = getById(id);
        recording.setIsArchived(false);
        recording.setArchiveDate(null);
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings updateBackupDate(Long id, Date backupDate) {
        log.info("Updating backup date for recording {}", id);
        
        Recordings recording = getById(id);
        recording.setBackupDate(backupDate);
        
        return recordingsRepository.save(recording);
    }

    // File management
    @Override
    public Recordings updateFilePath(Long id, String filePath) {
        log.info("Updating file path for recording {}", id);
        
        Recordings recording = getById(id);
        recording.setFilePath(filePath);
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings updateFileName(Long id, String fileName) {
        log.info("Updating file name for recording {}", id);
        
        Recordings recording = getById(id);
        recording.setFileName(fileName);
        
        return recordingsRepository.save(recording);
    }

    @Override
    public Recordings updateFileSize(Long id, Double fileSizeMb) {
        log.info("Updating file size for recording {}", id);
        
        Recordings recording = getById(id);
        recording.setFileSizeMb(fileSizeMb);
        
        return recordingsRepository.save(recording);
    }

    // Status management
    @Override
    public Recordings changeStatus(Long id, RecordingStatus newStatus) {
        log.info("Changing status of recording {} to {}", id, newStatus);
        
        Recordings recording = getById(id);
        recording.setStatus(newStatus);
        
        return recordingsRepository.save(recording);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getRecordingsNeedingBackup() {
        return recordingsRepository.findByStatusAndBackupDateIsNull(RecordingStatus.RECORDING);
    }

    // Reporting methods
    @Override
    @Transactional(readOnly = true)
    public Map<RecordingStatus, Long> getRecordingCountByStatus() {
        List<Object[]> results = recordingsRepository.countRecordingsByStatus();
        Map<RecordingStatus, Long> statusCounts = new HashMap<>();
        
        for (Object[] result : results) {
            RecordingStatus status = (RecordingStatus) result[0];
            Long count = (Long) result[1];
            statusCounts.put(status, count);
        }
        
        return statusCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRecordingCountByJudge() {
        List<Object[]> results = recordingsRepository.countRecordingsByJudge();
        Map<String, Long> judgeCounts = new HashMap<>();
        
        for (Object[] result : results) {
            String judge = (String) result[0];
            Long count = (Long) result[1];
            judgeCounts.put(judge, count);
        }
        
        return judgeCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRecordingCountByCaseType() {
        List<Object[]> results = recordingsRepository.countRecordingsByCaseType();
        Map<String, Long> caseTypeCounts = new HashMap<>();
        
        for (Object[] result : results) {
            String caseType = (String) result[0];
            Long count = (Long) result[1];
            caseTypeCounts.put(caseType, count);
        }
        
        return caseTypeCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalRecordingCount() {
        return recordingsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getRecordingsCountByStatus(RecordingStatus status) {
        return (long) recordingsRepository.findByStatus(status).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getArchivedRecordingsCount() {
        return (long) recordingsRepository.findByIsArchived(true).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNonArchivedRecordingsCount() {
        return (long) recordingsRepository.findByIsArchived(false).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalDuration() {
        Double total = recordingsRepository.getTotalDuration();
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalFileSize() {
        Double total = recordingsRepository.getTotalFileSize();
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageDuration() {
        List<Recordings> recordings = recordingsRepository.findAll();
        return recordings.stream()
            .filter(r -> r.getDurationMinutes() != null)
            .mapToDouble(Recordings::getDurationMinutes)
            .average()
            .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageFileSize() {
        List<Recordings> recordings = recordingsRepository.findAll();
        return recordings.stream()
            .filter(r -> r.getFileSizeMb() != null)
            .mapToDouble(Recordings::getFileSizeMb)
            .average()
            .orElse(0.0);
    }

    // Time-based queries
    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getRecordingsCreatedToday() {
        return recordingsRepository.findRecordingsCreatedToday();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getRecordingsCreatedThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return recordingsRepository.findRecordingsCreatedThisWeek(calendar.getTime());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getRecordingsCreatedThisMonth() {
        return recordingsRepository.findRecordingsCreatedThisMonth();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recordings> getRecordingsCreatedBetween(Date startDate, Date endDate) {
        return recordingsRepository.findByRecordingDateTimeBetween(startDate, endDate);
    }

    // Statistics
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRecordingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalRecordings", getTotalRecordingCount());
        stats.put("totalDuration", getTotalDuration());
        stats.put("totalFileSize", getTotalFileSize());
        stats.put("averageDuration", getAverageDuration());
        stats.put("averageFileSize", getAverageFileSize());
        stats.put("archivedCount", getArchivedRecordingsCount());
        stats.put("nonArchivedCount", getNonArchivedRecordingsCount());
        stats.put("statusCounts", getRecordingCountByStatus());
        stats.put("judgeCounts", getRecordingCountByJudge());
        stats.put("caseTypeCounts", getRecordingCountByCaseType());
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRecordingStatisticsByJudge(String judge) {
        List<Recordings> judgeRecordings = recordingsRepository.findByJudge(judge);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("judge", judge);
        stats.put("totalRecordings", (long) judgeRecordings.size());
        stats.put("totalDuration", judgeRecordings.stream()
            .filter(r -> r.getDurationMinutes() != null)
            .mapToDouble(Recordings::getDurationMinutes)
            .sum());
        stats.put("totalFileSize", judgeRecordings.stream()
            .filter(r -> r.getFileSizeMb() != null)
            .mapToDouble(Recordings::getFileSizeMb)
            .sum());
        stats.put("averageDuration", judgeRecordings.stream()
            .filter(r -> r.getDurationMinutes() != null)
            .mapToDouble(Recordings::getDurationMinutes)
            .average()
            .orElse(0.0));
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRecordingStatisticsByCaseType(String caseType) {
        List<Recordings> caseTypeRecordings = recordingsRepository.findByCaseType(caseType);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("caseType", caseType);
        stats.put("totalRecordings", (long) caseTypeRecordings.size());
        stats.put("totalDuration", caseTypeRecordings.stream()
            .filter(r -> r.getDurationMinutes() != null)
            .mapToDouble(Recordings::getDurationMinutes)
            .sum());
        stats.put("totalFileSize", caseTypeRecordings.stream()
            .filter(r -> r.getFileSizeMb() != null)
            .mapToDouble(Recordings::getFileSizeMb)
            .sum());
        stats.put("averageDuration", caseTypeRecordings.stream()
            .filter(r -> r.getDurationMinutes() != null)
            .mapToDouble(Recordings::getDurationMinutes)
            .average()
            .orElse(0.0));
        
        return stats;
    }

    // Bulk operations
    @Override
    public List<Recordings> bulkUpdateStatus(List<Long> ids, RecordingStatus status) {
        log.info("Bulk updating status for {} recordings to {}", ids.size(), status);
        
        List<Recordings> recordings = recordingsRepository.findAllById(ids);
        recordings.forEach(recording -> recording.setStatus(status));
        
        return recordingsRepository.saveAll(recordings);
    }

    @Override
    public List<Recordings> bulkArchive(List<Long> ids) {
        log.info("Bulk archiving {} recordings", ids.size());
        
        List<Recordings> recordings = recordingsRepository.findAllById(ids);
        recordings.forEach(recording -> {
            recording.setIsArchived(true);
            recording.setArchiveDate(new Date());
        });
        
        return recordingsRepository.saveAll(recordings);
    }

    @Override
    public List<Recordings> bulkUnarchive(List<Long> ids) {
        log.info("Bulk unarchiving {} recordings", ids.size());
        
        List<Recordings> recordings = recordingsRepository.findAllById(ids);
        recordings.forEach(recording -> {
            recording.setIsArchived(false);
            recording.setArchiveDate(null);
        });
        
        return recordingsRepository.saveAll(recordings);
    }

    @Override
    public void bulkDelete(List<Long> ids) {
        log.info("Bulk deleting {} recordings", ids.size());
        
        recordingsRepository.deleteAllById(ids);
    }

    private final Path uploadDir = Paths.get("uploads/audio");

    public Recordings createRecording(
            String caseDetails,
            String judge,
            Double durationMinutes,
            String caseType,
            String courtRoom,
            String notes,
            MultipartFile audioFile
    ) throws IOException {

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename
        String originalName = audioFile.getOriginalFilename();
        String extension = "";

        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex); // includes the dot
            originalName = originalName.substring(0, dotIndex); // remove extension from base name
        }

        String fileName = originalName + "_" + System.currentTimeMillis() + extension;
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(audioFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Calculate file size in MB
        double fileSizeMb = audioFile.getSize() / (1024.0 * 1024.0);

        Recordings recording = Recordings.builder()
                .recordingDateTime(new Date())
                .caseDetails(caseDetails)
                .judge(judge)
                .durationMinutes(durationMinutes)
                .caseType(caseType)
                .courtRoom(courtRoom)
                .notes(notes)
                .status(RecordingStatus.COMPLETED)
                .fileSizeMb(fileSizeMb)
                .filePath(filePath.toString())
                .fileName(fileName)
                .build();

        return recordingsRepository.save(recording);
    }

    public ResponseEntity<Resource> streamAudio(Long id) throws IOException {
        Recordings recording = recordingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recording not found"));

        Path path = Paths.get(recording.getFilePath());
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found on server");
        }

        // Load as resource
        Resource resource = new UrlResource(path.toUri());

        // Detect MIME type (optional)
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 🔥 Use original file name from DB for download header
        String originalFileName = recording.getFileName(); // this already contains your original file name

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + originalFileName + "\"")
                .body(resource);
    }

    private String detectContentType(String filename) {
        if (filename == null) return "application/octet-stream";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".mp3")) return "audio/mpeg";
        if (lower.endsWith(".wav")) return "audio/wav";
        if (lower.endsWith(".ogg")) return "audio/ogg";
        return "application/octet-stream";
    }
}
