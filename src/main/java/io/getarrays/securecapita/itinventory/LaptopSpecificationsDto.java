package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaptopSpecificationsDto {
    
    @NotNull(message = "RAM is required")
    @Min(value = 1, message = "RAM must be at least 1 GB")
    @Max(value = 1024, message = "RAM cannot exceed 1024 GB")
    private Integer ram;
    
    @NotNull(message = "Processor is required")
    @Min(value = 1, message = "Processor value must be positive")
    private Integer processor;
} 