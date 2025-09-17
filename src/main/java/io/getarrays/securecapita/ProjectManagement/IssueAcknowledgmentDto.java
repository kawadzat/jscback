package io.getarrays.securecapita.ProjectManagement;

import io.getarrays.securecapita.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueAcknowledgmentDto {

    private Long id;
    private Long issueId;
    private String issueTitle;
    private UserDTO acknowledgedBy;
    private LocalDateTime acknowledgmentDate;
    private String notes;
    private String signatureType;
    private LocalDateTime signatureTimestamp;
    private String ipAddress;
    private String userAgent;
    private String certificateInfo;
    private String signature;
    private String signatureHash;
}



