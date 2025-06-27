package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.*;
import io.getarrays.securecapita.antivirus.Antivirus;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.maintenance.Maintenance;
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
public class Laptop   extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Purchase date is required")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date purchaseDate;


    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer name must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100, nullable = false)
    private String manufacturer;

    @NotBlank(message = "Serial number is required")
    @Size(max = 50, message = "Serial number must not exceed 50 characters")
    @Column(name = "serial_number", length = 50, nullable = false, unique = true)
    private String serialNumber;



    @NotNull(message = "RAM is required")
    @Min(value = 1, message = "RAM must be at least 1 GB")
    @Max(value = 1024, message = "RAM cannot exceed 1024 GB")
    @Column(name = "ram", nullable = false)
    private Integer ram;

    @NotNull(message = "Processor is required")
    @Min(value = 1, message = "Processor value must be positive")
    @Column(name = "processor", nullable = false)
    private Integer processor;

    @NotNull(message = "Issue date is required")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issue_date", nullable = false)
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
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private LaptopStatus status;

    @NotBlank(message = "IssuedTo is required")
    @Size(max = 100, message = "IssuedTo name must not exceed 100 characters")
    @Column(name = "issued_to", length = 100, nullable = false)
    private String issuedTo;


    @NotBlank(message = "department is required")
    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "department", length = 100, nullable = false)
    private String department;


    @NotBlank(message = "designation is required")
    @Size(max = 100, message = "department name must not exceed 100 characters")
    @Column(name = "designation", length = 100, nullable = false)
    private String designation;


    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @NotNull(message = "Replacement date is required")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "replacement_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date replacementDate;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Maintenance> maintenanceList = new ArrayList<>();

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Antivirus> antivirusList = new ArrayList<>();


}

