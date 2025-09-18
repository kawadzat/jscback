package io.getarrays.securecapita.recordings;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Entity representing a court recording session.
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "recording_sessions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Recording extends Auditable<String> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "case_details", nullable = false)
    @NotNull(message = "Case details is required")
    private String caseDetails;
    
    @Column(name = "judge_name", nullable = false)
    @NotNull(message = "Judge is required")
    private String judge;
    
    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "duration_seconds", nullable = false)
    @NotNull(message = "Duration is required")
    private Integer duration; // in seconds
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recording_status")
    private RecordingStatus status;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_path")
    private String filePath;
    
    // Constructors
    public Recording(String caseDetails, String judge, LocalDateTime startTime, Integer duration) {
        this.caseDetails = caseDetails;
        this.judge = judge;
        this.startTime = startTime;
        this.duration = duration;
    }
    
    // Getters and setters are provided by Lombok @Getter and @Setter annotations
}
