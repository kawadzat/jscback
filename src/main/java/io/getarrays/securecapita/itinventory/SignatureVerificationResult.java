package io.getarrays.securecapita.itinventory;

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
public class SignatureVerificationResult {
    private boolean isValid;
    private String errorMessage;
    private LocalDateTime signatureTimestamp;
    private UserDTO signedBy;
    private Long laptopId;
    private String signatureHash;
    private String verificationMethod;
} 