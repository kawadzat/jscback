package io.getarrays.securecapita.itinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureStatistics {
    private long totalSignatures;
    private long validSignatures;
    private long invalidSignatures;
    private LocalDateTime lastSignatureDate;
    private double validityRate;
    
    public double getValidityRate() {
        if (totalSignatures == 0) {
            return 0.0;
        }
        return (double) validSignatures / totalSignatures * 100.0;
    }
} 