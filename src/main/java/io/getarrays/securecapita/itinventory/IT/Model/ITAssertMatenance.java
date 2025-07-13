package io.getarrays.securecapita.itinventory.IT.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itinventory.Laptop;
import io.getarrays.securecapita.maintenance.MaintenancePriority;
import io.getarrays.securecapita.maintenance.MaintenanceStatus;
import io.getarrays.securecapita.maintenance.MaintenanceType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "ITAssertMatenance")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class ITAssertMatenance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "itassertmaintenance_type")
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
    @JoinColumn(name = "itAssert_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ITAssert itAssert;

    // JPA lifecycle callback to ensure required fields are set
    @PrePersist
    @PreUpdate
    public void validateAndSetDefaults() {
        // Ensure description is never null or empty
        if (description == null || description.trim().isEmpty()) {
            if (itAssert != null && itAssert.getId() != null) {
                description = "Scheduled maintenance for laptop " + itAssert.getId();
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
