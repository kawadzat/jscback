package io.getarrays.securecapita.recordings;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Recordings entity.
 * Provides endpoints for court recordings management.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/v1/recordings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecordingsController {

    private final RecordingsService recordingsService;

    // Basic CRUD operations
    @PostMapping
    public ResponseEntity<Recordings> create(@RequestBody Recordings recording) {
        return new ResponseEntity<>(recordingsService.createWithDuplicateCheck(recording), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recordings> update(@PathVariable Long id, @RequestBody Recordings recording) {
        return ResponseEntity.ok(recordingsService.updateWithDuplicateCheck(id, recording));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recordings> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Recordings>> getAll() {
        return ResponseEntity.ok(recordingsService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordingsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search and filter endpoints
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Recordings>> getByStatus(@PathVariable RecordingStatus status) {
        return ResponseEntity.ok(recordingsService.getByStatus(status));
    }

    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<Recordings>> getByStatusWithPagination(
            @PathVariable RecordingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recordingDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(recordingsService.getByStatus(status, pageable));
    }

    @GetMapping("/judge/{judge}")
    public ResponseEntity<List<Recordings>> getByJudge(@PathVariable String judge) {
        return ResponseEntity.ok(recordingsService.getByJudge(judge));
    }

    @GetMapping("/judge/{judge}/page")
    public ResponseEntity<Page<Recordings>> getByJudgeWithPagination(
            @PathVariable String judge,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recordingDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(recordingsService.getByJudge(judge, pageable));
    }

    @GetMapping("/case-details")
    public ResponseEntity<List<Recordings>> getByCaseDetails(@RequestParam String caseDetails) {
        return ResponseEntity.ok(recordingsService.getByCaseDetails(caseDetails));
    }

    @GetMapping("/case-type/{caseType}")
    public ResponseEntity<List<Recordings>> getByCaseType(@PathVariable String caseType) {
        return ResponseEntity.ok(recordingsService.getByCaseType(caseType));
    }

    @GetMapping("/court-room/{courtRoom}")
    public ResponseEntity<List<Recordings>> getByCourtRoom(@PathVariable String courtRoom) {
        return ResponseEntity.ok(recordingsService.getByCourtRoom(courtRoom));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Recordings>> getByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return ResponseEntity.ok(recordingsService.getByDateRange(startDate, endDate));
    }

    @GetMapping("/date-range/page")
    public ResponseEntity<Page<Recordings>> getByDateRangeWithPagination(
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recordingDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(recordingsService.getByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/duration-range")
    public ResponseEntity<List<Recordings>> getByDurationRange(
            @RequestParam Double minDuration,
            @RequestParam Double maxDuration) {
        return ResponseEntity.ok(recordingsService.getByDurationRange(minDuration, maxDuration));
    }

    @GetMapping("/file-size-range")
    public ResponseEntity<List<Recordings>> getByFileSizeRange(
            @RequestParam Double minSize,
            @RequestParam Double maxSize) {
        return ResponseEntity.ok(recordingsService.getByFileSizeRange(minSize, maxSize));
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Recordings>> getArchivedRecordings() {
        return ResponseEntity.ok(recordingsService.getArchivedRecordings());
    }

    @GetMapping("/archived/page")
    public ResponseEntity<Page<Recordings>> getArchivedRecordingsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recordingDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(recordingsService.getArchivedRecordings(pageable));
    }

    @GetMapping("/non-archived")
    public ResponseEntity<List<Recordings>> getNonArchivedRecordings() {
        return ResponseEntity.ok(recordingsService.getNonArchivedRecordings());
    }

    // Advanced search endpoint
    @GetMapping("/search")
    public ResponseEntity<Page<Recordings>> searchRecordings(
            @RequestParam(required = false) String caseDetails,
            @RequestParam(required = false) String judge,
            @RequestParam(required = false) String caseType,
            @RequestParam(required = false) RecordingStatus status,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recordingDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(recordingsService.searchRecordings(
            caseDetails, judge, caseType, status, isArchived, pageable));
    }

    // Business logic endpoints
    @PutMapping("/{id}/mark-backed")
    public ResponseEntity<Recordings> markAsBacked(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.markAsBacked(id));
    }

    @PutMapping("/{id}/mark-processing")
    public ResponseEntity<Recordings> markAsProcessing(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.markAsProcessing(id));
    }

    @PutMapping("/{id}/mark-failed")
    public ResponseEntity<Recordings> markAsFailed(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.markAsFailed(id));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Recordings> archiveRecording(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.archiveRecording(id));
    }

    @PutMapping("/{id}/unarchive")
    public ResponseEntity<Recordings> unarchiveRecording(@PathVariable Long id) {
        return ResponseEntity.ok(recordingsService.unarchiveRecording(id));
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<Recordings> changeStatus(
            @PathVariable Long id,
            @RequestParam RecordingStatus newStatus) {
        return ResponseEntity.ok(recordingsService.changeStatus(id, newStatus));
    }

    @GetMapping("/needing-backup")
    public ResponseEntity<List<Recordings>> getRecordingsNeedingBackup() {
        return ResponseEntity.ok(recordingsService.getRecordingsNeedingBackup());
    }

    // File management endpoints
    @PutMapping("/{id}/file-path")
    public ResponseEntity<Recordings> updateFilePath(
            @PathVariable Long id,
            @RequestParam String filePath) {
        return ResponseEntity.ok(recordingsService.updateFilePath(id, filePath));
    }

    @PutMapping("/{id}/file-name")
    public ResponseEntity<Recordings> updateFileName(
            @PathVariable Long id,
            @RequestParam String fileName) {
        return ResponseEntity.ok(recordingsService.updateFileName(id, fileName));
    }

    @PutMapping("/{id}/file-size")
    public ResponseEntity<Recordings> updateFileSize(
            @PathVariable Long id,
            @RequestParam Double fileSizeMb) {
        return ResponseEntity.ok(recordingsService.updateFileSize(id, fileSizeMb));
    }

    @PutMapping("/{id}/backup-date")
    public ResponseEntity<Recordings> updateBackupDate(
            @PathVariable Long id,
            @RequestParam Date backupDate) {
        return ResponseEntity.ok(recordingsService.updateBackupDate(id, backupDate));
    }

    // Reporting endpoints
    @GetMapping("/reports/status-counts")
    public ResponseEntity<Map<RecordingStatus, Long>> getRecordingCountByStatus() {
        return ResponseEntity.ok(recordingsService.getRecordingCountByStatus());
    }

    @GetMapping("/reports/judge-counts")
    public ResponseEntity<Map<String, Long>> getRecordingCountByJudge() {
        return ResponseEntity.ok(recordingsService.getRecordingCountByJudge());
    }

    @GetMapping("/reports/case-type-counts")
    public ResponseEntity<Map<String, Long>> getRecordingCountByCaseType() {
        return ResponseEntity.ok(recordingsService.getRecordingCountByCaseType());
    }

    @GetMapping("/reports/total-count")
    public ResponseEntity<Long> getTotalRecordingCount() {
        return ResponseEntity.ok(recordingsService.getTotalRecordingCount());
    }

    @GetMapping("/reports/status-count/{status}")
    public ResponseEntity<Long> getRecordingsCountByStatus(@PathVariable RecordingStatus status) {
        return ResponseEntity.ok(recordingsService.getRecordingsCountByStatus(status));
    }

    @GetMapping("/reports/archived-count")
    public ResponseEntity<Long> getArchivedRecordingsCount() {
        return ResponseEntity.ok(recordingsService.getArchivedRecordingsCount());
    }

    @GetMapping("/reports/non-archived-count")
    public ResponseEntity<Long> getNonArchivedRecordingsCount() {
        return ResponseEntity.ok(recordingsService.getNonArchivedRecordingsCount());
    }

    @GetMapping("/reports/total-duration")
    public ResponseEntity<Double> getTotalDuration() {
        return ResponseEntity.ok(recordingsService.getTotalDuration());
    }

    @GetMapping("/reports/total-file-size")
    public ResponseEntity<Double> getTotalFileSize() {
        return ResponseEntity.ok(recordingsService.getTotalFileSize());
    }

    @GetMapping("/reports/average-duration")
    public ResponseEntity<Double> getAverageDuration() {
        return ResponseEntity.ok(recordingsService.getAverageDuration());
    }

    @GetMapping("/reports/average-file-size")
    public ResponseEntity<Double> getAverageFileSize() {
        return ResponseEntity.ok(recordingsService.getAverageFileSize());
    }

    // Time-based query endpoints
    @GetMapping("/today")
    public ResponseEntity<List<Recordings>> getRecordingsCreatedToday() {
        return ResponseEntity.ok(recordingsService.getRecordingsCreatedToday());
    }

    @GetMapping("/this-week")
    public ResponseEntity<List<Recordings>> getRecordingsCreatedThisWeek() {
        return ResponseEntity.ok(recordingsService.getRecordingsCreatedThisWeek());
    }

    @GetMapping("/this-month")
    public ResponseEntity<List<Recordings>> getRecordingsCreatedThisMonth() {
        return ResponseEntity.ok(recordingsService.getRecordingsCreatedThisMonth());
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<Recordings>> getRecordingsCreatedBetween(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return ResponseEntity.ok(recordingsService.getRecordingsCreatedBetween(startDate, endDate));
    }

    // Statistics endpoints
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getRecordingStatistics() {
        return ResponseEntity.ok(recordingsService.getRecordingStatistics());
    }

    @GetMapping("/statistics/judge/{judge}")
    public ResponseEntity<Map<String, Object>> getRecordingStatisticsByJudge(@PathVariable String judge) {
        return ResponseEntity.ok(recordingsService.getRecordingStatisticsByJudge(judge));
    }

    @GetMapping("/statistics/case-type/{caseType}")
    public ResponseEntity<Map<String, Object>> getRecordingStatisticsByCaseType(@PathVariable String caseType) {
        return ResponseEntity.ok(recordingsService.getRecordingStatisticsByCaseType(caseType));
    }

    // Bulk operations endpoints
    @PutMapping("/bulk/status")
    public ResponseEntity<List<Recordings>> bulkUpdateStatus(
            @RequestBody List<Long> ids,
            @RequestParam RecordingStatus status) {
        return ResponseEntity.ok(recordingsService.bulkUpdateStatus(ids, status));
    }

    @PutMapping("/bulk/archive")
    public ResponseEntity<List<Recordings>> bulkArchive(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(recordingsService.bulkArchive(ids));
    }

    @PutMapping("/bulk/unarchive")
    public ResponseEntity<List<Recordings>> bulkUnarchive(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(recordingsService.bulkUnarchive(ids));
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Long> ids) {
        recordingsService.bulkDelete(ids);
        return ResponseEntity.noContent().build();
    }

    // Duplicate check endpoints
    @GetMapping("/check/file-name/{fileName}")
    public ResponseEntity<Map<String, Object>> checkFileNameExists(@PathVariable String fileName) {
        boolean exists = recordingsService.isFileNameExists(fileName);
        Map<String, Object> response = Map.of(
            "fileName", fileName,
            "exists", exists,
            "message", exists ? "File name already exists" : "File name is available"
        );
        return ResponseEntity.ok(response);
    }

    // File download endpoints
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadRecording(@PathVariable Long id) {
        try {
            Recordings recording = recordingsService.getById(id);
            if (recording == null) {
                return ResponseEntity.notFound().build();
            }
            
            if (recording.getFilePath() == null || recording.getFileName() == null) {
                return ResponseEntity.badRequest()
                        .body("Recording has no file path or file name".getBytes());
            }

            // Try multiple possible file paths
            Path filePath = resolveFilePath(recording.getFilePath(), recording.getFileName());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Check if it's a file (not directory)
            if (!Files.isRegularFile(filePath)) {
                return ResponseEntity.badRequest().build();
            }

            byte[] audioData = Files.readAllBytes(filePath);
            
            // Determine content type based on file extension
            String contentType = getContentType(recording.getFileName());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + recording.getFileName() + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                    .contentType(MediaType.valueOf(contentType))
                    .contentLength(audioData.length)
                    .body(audioData);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadRecordingByFileName(@PathVariable String fileName) {
        try {
            // Find recording by file name
            List<Recordings> recordings = recordingsService.getAll();
            Recordings recording = recordings.stream()
                    .filter(r -> fileName.equals(r.getFileName()))
                    .findFirst()
                    .orElse(null);
            
            if (recording == null) {
                return ResponseEntity.notFound().build();
            }

            return downloadRecording(recording.getId());
            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<StreamingResponseBody> streamRecording(@PathVariable Long id) {
        try {
            Recordings recording = recordingsService.getById(id);
            if (recording == null || recording.getFilePath() == null || recording.getFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = resolveFilePath(recording.getFilePath(), recording.getFileName());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            StreamingResponseBody stream = outputStream -> {
                try {
                    Files.copy(filePath, outputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Error streaming file", e);
                }
            };

            String contentType = getContentType(recording.getFileName());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + recording.getFileName() + "\"")
                    .contentType(MediaType.valueOf(contentType))
                    .contentLength(Files.size(filePath))
                    .body(stream);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/play")
    public ResponseEntity<StreamingResponseBody> playRecording(@PathVariable Long id) {
        try {
            Recordings recording = recordingsService.getById(id);
            if (recording == null || recording.getFilePath() == null || recording.getFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = resolveFilePath(recording.getFilePath(), recording.getFileName());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            StreamingResponseBody stream = outputStream -> {
                try {
                    Files.copy(filePath, outputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Error streaming file", e);
                }
            };

            String contentType = getContentType(recording.getFileName());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recording.getFileName() + "\"")
                    .contentType(MediaType.valueOf(contentType))
                    .contentLength(Files.size(filePath))
                    .body(stream);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create test recording endpoint
    @PostMapping("/create-test")
    public ResponseEntity<Recordings> createTestRecording() {
        try {
            Recordings testRecording = Recordings.builder()
                    .recordingDateTime(new Date())
                    .caseDetails("Test Voice Recording")
                    .judge("Test Judge")
                    .durationMinutes(5.0)
                    .status(RecordingStatus.NOT_BACKED)
                    .fileSizeMb(2.5)
                    .filePath("recordings/test/voice_test.wav")
                    .fileName("voice_test.wav")
                    .notes("Test recording for download functionality")
                    .caseType("VOICE_TESTIMONY")
                    .courtRoom("Test Room")
                    .isArchived(false)
                    .build();
            
            Recordings created = recordingsService.createWithDuplicateCheck(testRecording);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Debug endpoint to check file existence
    @GetMapping("/{id}/debug")
    public ResponseEntity<Map<String, Object>> debugFileLocation(@PathVariable Long id) {
        try {
            Recordings recording = recordingsService.getById(id);
            if (recording == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> debugInfo = Map.of(
                "recordingId", recording.getId(),
                "fileName", recording.getFileName() != null ? recording.getFileName() : "null",
                "filePath", recording.getFilePath() != null ? recording.getFilePath() : "null",
                "userHome", System.getProperty("user.home"),
                "fileLocations", checkFileLocations(recording.getFilePath(), recording.getFileName())
            );

            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Helper methods
    private Map<String, Object> checkFileLocations(String filePath, String fileName) {
        Map<String, Object> locations = new java.util.HashMap<>();
        
        // Try the full path first
        Path fullPath = Paths.get(filePath);
        locations.put("fullPath", Map.of(
            "path", fullPath.toString(),
            "exists", Files.exists(fullPath)
        ));

        // Try relative to user home directory
        Path homePath = Paths.get(System.getProperty("user.home"), filePath);
        locations.put("homePath", Map.of(
            "path", homePath.toString(),
            "exists", Files.exists(homePath)
        ));

        // Try relative to server root
        Path rootPath = Paths.get(filePath.startsWith("/") ? filePath : "/" + filePath);
        locations.put("rootPath", Map.of(
            "path", rootPath.toString(),
            "exists", Files.exists(rootPath)
        ));

        // Try in recordings directory under user home
        Path recordingsPath = Paths.get(System.getProperty("user.home"), "recordings", filePath);
        locations.put("recordingsPath", Map.of(
            "path", recordingsPath.toString(),
            "exists", Files.exists(recordingsPath)
        ));

        // Try in downloads directory (like images)
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads", "recordings", filePath);
        locations.put("downloadsPath", Map.of(
            "path", downloadsPath.toString(),
            "exists", Files.exists(downloadsPath)
        ));

        return locations;
    }

    private Path resolveFilePath(String filePath, String fileName) {
        // Try the full path first
        Path fullPath = Paths.get(filePath);
        if (Files.exists(fullPath) && Files.isRegularFile(fullPath)) {
            return fullPath;
        }

        // Try relative to user home directory
        Path homePath = Paths.get(System.getProperty("user.home"), filePath);
        if (Files.exists(homePath) && Files.isRegularFile(homePath)) {
            return homePath;
        }

        // Try relative to server root
        Path rootPath = Paths.get(filePath.startsWith("/") ? filePath : "/" + filePath);
        if (Files.exists(rootPath) && Files.isRegularFile(rootPath)) {
            return rootPath;
        }

        // Try in recordings directory under user home
        Path recordingsPath = Paths.get(System.getProperty("user.home"), "recordings", filePath);
        if (Files.exists(recordingsPath) && Files.isRegularFile(recordingsPath)) {
            return recordingsPath;
        }

        // Try in downloads directory (like images)
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads", "recordings", filePath);
        if (Files.exists(downloadsPath) && Files.isRegularFile(downloadsPath)) {
            return downloadsPath;
        }

        // Try in current working directory
        Path currentPath = Paths.get(System.getProperty("user.dir"), filePath);
        if (Files.exists(currentPath) && Files.isRegularFile(currentPath)) {
            return currentPath;
        }

        // Try in src/main/resources (for development)
        Path resourcesPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "recordings", filePath);
        if (Files.exists(resourcesPath) && Files.isRegularFile(resourcesPath)) {
            return resourcesPath;
        }

        // Try just the filename in user home
        if (fileName != null) {
            Path fileNamePath = Paths.get(System.getProperty("user.home"), fileName);
            if (Files.exists(fileNamePath) && Files.isRegularFile(fileNamePath)) {
                return fileNamePath;
            }
        }

        // Return the original path (will fail if not found)
        return fullPath;
    }

    private String getContentType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".wav")) {
            return "audio/wav";
        } else if (lowerFileName.endsWith(".mp3")) {
            return "audio/mpeg";
        } else if (lowerFileName.endsWith(".m4a")) {
            return "audio/mp4";
        } else if (lowerFileName.endsWith(".ogg")) {
            return "audio/ogg";
        } else if (lowerFileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowerFileName.endsWith(".avi")) {
            return "video/x-msvideo";
        } else {
            return "application/octet-stream";
        }
    }
}

