package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.*;
import io.getarrays.securecapita.antivirus.Antivirus;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.itinventory.validation.LaptopConditionalValidation;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "laptop")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
@LaptopConditionalValidation
public class Laptop   extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date purchaseDate;

    @Size(max = 100, message = "Manufacturer name must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100, nullable = true)
    private String manufacturer;

    @Size(max = 100, message = "Assert type must not exceed 100 characters")
    @Column(name = "assert_type", length = 100, nullable = true)
    private String assertType;

    @Size(max = 50, message = "Serial number must not exceed 50 characters")
    @Column(name = "serial_number", length = 50, nullable = true, unique = true)
    private String serialNumber;

    @Size(max = 20, message = "IMEI must not exceed 20 characters")
    @Column(name = "imei", length = 20, nullable = true, unique = true)
    private String imei;

    // Laptop-specific fields - nullable for non-laptop assets
    @Min(value = 1, message = "RAM must be at least 1 GB")
    @Max(value = 1024, message = "RAM cannot exceed 1024 GB")
    @Column(name = "ram", nullable = true)
    private Integer ram;

    @Min(value = 1, message = "Processor value must be positive")
    @Column(name = "processor", nullable = true)
    private Integer processor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issue_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date issueDate;

    /**
     * Current status of the laptop.
     * Possible values:
     * - AVAILABLE: Laptop is available for issue
     * - ISSUED: Laptop is currently issued to someone
     * - MAINTENANCE: Laptop is under maintenance
     * - RETIRED: Laptop is no longer in use
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = true)
    private LaptopStatus status;

    @Size(max = 100, message = "IssuedTo name must not exceed 100 characters")
    @Column(name = "issued_to", length = 100, nullable = true)
    private String issuedTo;

    @Size(max = 100, message = "station name must not exceed 100 characters")
    @Column(name = "station", length = 100, nullable = true)
    private String station;

    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "department", length = 100, nullable = true)
    private String department;

    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "designation", length = 100, nullable = true)
    private String designation;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(name = "email", length = 255, nullable = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "replacement_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date replacementDate;


    @Size(max = 100, message = "licence name must not exceed 100 characters")
    @Column(name = "licence", length = 100, nullable = true)
    private String licence;



    @Column(name = "issue_by_email", length = 255)
    private String issueByEmail;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Maintenance> maintenanceList = new ArrayList<>();

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Antivirus> antivirusList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = LaptopStatus.AVAILABLE;
        }
    }
}

