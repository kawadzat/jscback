package io.getarrays.securecapita.recordings;

/**
 * Enum representing the status of a court recording.
 * Based on the Testimony App interface showing "not backed" status.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
public enum RecordingStatus {
    /**
     * Recording has been created but not yet backed up
     */
    NOT_BACKED,
    
    /**
     * Recording has been successfully backed up
     */
    BACKED,
    
    /**
     * Recording is currently being processed
     */
    PROCESSING,
    
    /**
     * Recording has failed during processing
     */
    FAILED,
    
    /**
     * Recording has been archived
     */
    ARCHIVED,
    
    /**
     * Recording has been deleted
     */
    DELETED
}
