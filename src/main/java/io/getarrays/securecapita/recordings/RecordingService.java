package io.getarrays.securecapita.recordings;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * Service interface for Recording entity operations.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
public interface RecordingService {
    
    // Basic CRUD operations
    Recording create(Recording recording);
    Recording update(Long id, Recording recording);
    Recording getById(Long id);
    List<Recording> getAll();
    void delete(Long id);
    
    // Status management
    Recording updateStatus(Long id, RecordingStatus status);
    List<Recording> bulkUpdateStatus(List<Long> ids, RecordingStatus status);
    List<Recording> getByStatus(RecordingStatus status);
    
    // Microphone recording operations
    Recording startRecording(StartRecordingRequest request);
    Recording stopRecording(Long id);
    Recording uploadAudioFile(Long id, MultipartFile audioFile);
    RecordingStatus getRecordingStatus(Long id);
    
    // Audio download operations
    ResponseEntity<byte[]> downloadAudioFile(Long id);
    ResponseEntity<StreamingResponseBody> streamAudioFile(Long id);
    AudioDownloadInfo getDownloadInfo(Long id);
}
