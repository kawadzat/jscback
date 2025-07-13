package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaptopAcknowledgmentDto {
    private Long id;
    private Long laptopId;
    private UserDTO acknowledgedBy;
    private LocalDateTime acknowledgmentDate;
    private String notes;
    private String signature; // Base64-encoded signature image
    private String signatureType;
    private LocalDateTime signatureTimestamp;
    private String ipAddress;
    private String userAgent;
    private String certificateInfo;
    private String signatureHash;
    private String laptopSerialNumber;
    private String laptopIssuedTo;
    private String laptopStation;
}
