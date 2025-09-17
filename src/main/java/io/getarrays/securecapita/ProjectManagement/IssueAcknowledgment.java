package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue_acknowledgments")
public class IssueAcknowledgment extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "acknowledged_by_id", nullable = false)
    private User acknowledgedBy;

    @Column(name = "acknowledgment_date")
    private LocalDateTime acknowledgmentDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "signature_type")
    private String signatureType;

    @Column(name = "signature_timestamp")
    private LocalDateTime signatureTimestamp;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "certificate_info")
    private String certificateInfo;

    @Column(name = "signature", columnDefinition = "TEXT")
    private String signature;

    @Column(name = "signature_hash")
    private String signatureHash;

}



