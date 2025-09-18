package io.getarrays.securecapita.recordings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for starting a microphone recording session.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartRecordingRequest {
    
    @NotBlank(message = "Case details is required")
    private String caseDetails;
    
    @NotBlank(message = "Judge name is required")
    private String judge;
    
    private String notes;
    
    private String caseType;
    
    private String courtRoom;
    
    // Optional: Device information
    private String deviceInfo;
    
    // Optional: Audio quality settings
    private String audioQuality; // HIGH, MEDIUM, LOW
    
    // Optional: Recording format
    private String recordingFormat; // WAV, MP3, M4A
}
