package io.getarrays.securecapita.maintenance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.itinventory.Laptop;
import jakarta.persistence.*;
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

    @Column(name = "maintenance_type")
    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;

    @Column(name = "description")
    private String description;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "technician_name")
    private String technicianName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private MaintenancePriority priority;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Laptop laptop;

    // JPA lifecycle callback to ensure required fields are set
    @PrePersist
    @PreUpdate
    public void validateAndSetDefaults() {
        // Ensure description is never null or empty
        if (description == null || description.trim().isEmpty()) {
            if (laptop != null && laptop.getId() != null) {
                description = "Scheduled maintenance for laptop " + laptop.getId();
            } else {
                description = "Scheduled maintenance task";
            }
        }
        
        // Ensure maintenance type is never null
        if (maintenanceType == null) {
            maintenanceType = MaintenanceType.PREVENTIVE;
        }
        
        // Ensure scheduled date is never null
        if (scheduledDate == null) {
            scheduledDate = LocalDateTime.now().plusDays(1);
        }
        
        // Ensure status is never null
        if (status == null) {
            status = MaintenanceStatus.SCHEDULED;
        }
        
        // Ensure priority is never null
        if (priority == null) {
            priority = MaintenancePriority.MEDIUM;
        }
    }
} 