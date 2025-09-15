package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.dto.UserDTO;
import lombok.*;

import jakarta.validation.constraints.Size;
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
    
    @Size(max = 50, message = "Signature type must not exceed 50 characters")
    private String signatureType;
    
    private LocalDateTime signatureTimestamp;
    
    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;
    
    @Size(max = 500, message = "User agent must not exceed 500 characters")
    private String userAgent;
    
    @Size(max = 1000, message = "Certificate info must not exceed 1000 characters")
    private String certificateInfo;
    
    @Size(max = 255, message = "Signature hash must not exceed 255 characters")
    private String signatureHash;
    
    private String laptopSerialNumber;
    private String laptopIssuedTo;
    private String laptopStation;
}
