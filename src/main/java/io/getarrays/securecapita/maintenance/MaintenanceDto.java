package io.getarrays.securecapita.maintenance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class MaintenanceDto {

    private Long id;

    // Make these optional - service will provide defaults
    private MaintenanceType maintenanceType;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String technicianName;
    private MaintenanceStatus status;
    private MaintenancePriority priority;
    private String notes;
    private Long laptopId;
}
