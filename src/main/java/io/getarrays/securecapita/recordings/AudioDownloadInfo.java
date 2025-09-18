package io.getarrays.securecapita.recordings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO containing information about audio file download.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioDownloadInfo {
    
    private Long recordingId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String mimeType;
    private String downloadUrl;
    private String streamUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private boolean fileExists;
    private String errorMessage;
    
    // File metadata
    private String caseDetails;
    private String judge;
    private Integer duration; // in seconds
    private RecordingStatus status;
}
