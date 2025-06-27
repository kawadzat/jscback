package io.getarrays.securecapita.maintenance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.itinventory.Laptop;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "maintenance")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class Maintenance extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Maintenance type is required")
    @Column(name = "maintenance_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Scheduled date is required")
    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "technician_name")
    private String technicianName;

    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private MaintenancePriority priority;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Laptop laptop;
} 