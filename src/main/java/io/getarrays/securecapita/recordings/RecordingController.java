package io.getarrays.securecapita.recordings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * REST Controller for the new Recording entity.
 * Provides endpoints for recording status management.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/v1/recording")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecordingController {

    private final RecordingService recordingService;

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Recording controller is working!");
    }

    // Microphone recording endpoints
    @PostMapping("/start-recording")
    public ResponseEntity<Recording> startRecording(@RequestBody StartRecordingRequest request) {
        return ResponseEntity.ok(recordingService.startRecording(request));
    }

    @PutMapping("/{id}/stop-recording")
    public ResponseEntity<Recording> stopRecording(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.stopRecording(id));
    }

    @PostMapping("/{id}/upload-audio")
    public ResponseEntity<Recording> uploadAudioFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile audioFile) {
        return ResponseEntity.ok(recordingService.uploadAudioFile(id, audioFile));
    }

    @GetMapping("/{id}/recording-status")
    public ResponseEntity<RecordingStatus> getRecordingStatus(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.getRecordingStatus(id));
    }

    // Audio download endpoints
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadAudio(@PathVariable Long id) {
        return recordingService.downloadAudioFile(id);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<StreamingResponseBody> streamAudio(@PathVariable Long id) {
        return recordingService.streamAudioFile(id);
    }

    @GetMapping("/{id}/download-info")
    public ResponseEntity<AudioDownloadInfo> getDownloadInfo(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.getDownloadInfo(id));
    }

    // Basic CRUD operations
    @PostMapping
    public ResponseEntity<Recording> create(@RequestBody Recording recording) {
        return new ResponseEntity<>(recordingService.create(recording), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recording> update(@PathVariable Long id, @RequestBody Recording recording) {
        return ResponseEntity.ok(recordingService.update(id, recording));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recording> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Recording>> getAll() {
        return ResponseEntity.ok(recordingService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Status management endpoints
    @PutMapping("/{id}/status")
    public ResponseEntity<Recording> updateStatus(
            @PathVariable Long id,
            @RequestParam RecordingStatus status) {
        return ResponseEntity.ok(recordingService.updateStatus(id, status));
    }

    @PutMapping("/{id}/mark-recording")
    public ResponseEntity<Recording> markAsRecording(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.updateStatus(id, RecordingStatus.RECORDING));
    }

    @PutMapping("/{id}/mark-completed")
    public ResponseEntity<Recording> markAsCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.updateStatus(id, RecordingStatus.COMPLETED));
    }

    @PutMapping("/{id}/mark-backed")
    public ResponseEntity<Recording> markAsBacked(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.updateStatus(id, RecordingStatus.BACKED));
    }

    @PutMapping("/{id}/mark-archived")
    public ResponseEntity<Recording> markAsArchived(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.updateStatus(id, RecordingStatus.ARCHIVED));
    }

    @PutMapping("/{id}/mark-deleted")
    public ResponseEntity<Recording> markAsDeleted(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.updateStatus(id, RecordingStatus.DELETED));
    }

    // Bulk status updates
    @PutMapping("/bulk/status")
    public ResponseEntity<List<Recording>> bulkUpdateStatus(
            @RequestBody List<Long> ids,
            @RequestParam RecordingStatus status) {
        return ResponseEntity.ok(recordingService.bulkUpdateStatus(ids, status));
    }

    // Get recordings by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Recording>> getByStatus(@PathVariable RecordingStatus status) {
        return ResponseEntity.ok(recordingService.getByStatus(status));
    }
}
