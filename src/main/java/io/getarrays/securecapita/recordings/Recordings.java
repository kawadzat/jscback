package io.getarrays.securecapita.recordings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Entity representing court recordings in the Testimony App system.
 * Based on the interface showing recordings with case details, judge information,
 * duration, status, and file size.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "recordings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Recordings extends Auditable<String> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Date and time when the recording was created
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "recording_date_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @NotNull(message = "Recording date and time is required")
    private Date recordingDateTime;

    /**
     * Case details or case number associated with the recording
     */
    @Size(max = 255, message = "Case details must not exceed 255 characters")
    @Column(name = "case_details", length = 255, nullable = true)
    private String caseDetails;

    /**
     * Name of the presiding judge
     */
    @Size(max = 100, message = "Judge name must not exceed 100 characters")
    @Column(name = "judge", length = 100, nullable = true)
    private String judge;

    /**
     * Duration of the recording in minutes
     */
    @DecimalMin(value = "0.0", message = "Duration must be non-negative")
    @Column(name = "duration_minutes", nullable = true)
    private Double durationMinutes;

    /**
     * Current status of the recording
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @NotNull(message = "Recording status is required")
    private RecordingStatus status;

    /**
     * File size of the recording in MB
     */
    @DecimalMin(value = "0.0", message = "File size must be non-negative")
    @Column(name = "file_size_mb", nullable = true)
    private Double fileSizeMb;

    /**
     * File path or URL where the recording is stored
     */
    @Size(max = 500, message = "File path must not exceed 500 characters")
    @Column(name = "file_path", length = 500, nullable = true)
    private String filePath;

    /**
     * File name of the recording
     */
    @Size(max = 255, message = "File name must not exceed 255 characters")
    @Column(name = "file_name", length = 255, nullable = true)
    private String fileName;

    /**
     * Additional notes or comments about the recording
     */
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000, nullable = true)
    private String notes;

    /**
     * Type of case (e.g., TRIAL, DIVORCE_SUMMONS, etc.)
     */
    @Size(max = 100, message = "Case type must not exceed 100 characters")
    @Column(name = "case_type", length = 100, nullable = true)
    private String caseType;

    /**
     * Court room or location where the recording was made
     */
    @Size(max = 100, message = "Court room must not exceed 100 characters")
    @Column(name = "court_room", length = 100, nullable = true)
    private String courtRoom;

    /**
     * Date when the recording was last backed up
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "backup_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date backupDate;

    /**
     * Whether the recording is archived
     */
    @Column(name = "is_archived", nullable = false)
    @Builder.Default
    private Boolean isArchived = false;

    /**
     * Date when the recording was archived
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "archive_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date archiveDate;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = RecordingStatus.RECORDING;
        }
        if (isArchived == null) {
            isArchived = false;
        }
    }
}
