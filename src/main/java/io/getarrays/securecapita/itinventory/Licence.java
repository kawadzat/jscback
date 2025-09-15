package io.getarrays.securecapita.itinventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entity class for managing software licenses
 */
@Entity
@Table(name = "licence")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Licence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assetType;

    @Size(max = 200, message = "License name must not exceed 200 characters")
    @Column(name = "license_name", length = 200, nullable = false)
    private String licenseName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500, nullable = true)
    private String description;


    @Size(max = 255, message = "License key must not exceed 255 characters")
    @Column(name = "license_key", length = 255, nullable = false, unique = true)
    private String licenseKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date purchaseDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "installation_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date installationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date expiryDate;

    @NotNull(message = "License type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", length = 50, nullable = false)
    private LicenseType licenseType;

    @NotNull(message = "License status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private LicenseStatus status;

    @Size(max = 100, message = "Supplier name must not exceed 100 characters")
    @Column(name = "supplier", length = 100, nullable = true)
    private String supplier;

    @Size(max = 100, message = "Vendor name must not exceed 100 characters")
    @Column(name = "vendor", length = 100, nullable = true)
    private String vendor;

    @Column(name = "number_of_seats", nullable = true)
    private Integer numberOfSeats;

    @Column(name = "price", nullable = true)
    private Double price;

    @Size(max = 50, message = "Currency must not exceed 50 characters")
    @Column(name = "currency", length = 50, nullable = true)
    private String currency;

    @Size(max = 100, message = "Version must not exceed 100 characters")
    @Column(name = "version", length = 100, nullable = true)
    private String version;

    @Size(max = 255, message = "Installation path must not exceed 255 characters")
    @Column(name = "installation_path", length = 255, nullable = true)
    private String installationPath;

    @Size(max = 255, message = "File path must not exceed 255 characters")
    @Column(name = "file_path", length = 255, nullable = true)
    private String filePath;
    @Column(name = "days_to_expiration")
    private Long daysToExpiration;


    @Size(max = 100, message = "Department must not exceed 100 characters")
    @Column(name = "department", length = 100, nullable = true)
    private String department;

    @Size(max = 100, message = "Station must not exceed 100 characters")
    @Column(name = "station", length = 100, nullable = true)
    private String station;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000, nullable = true)
    private String notes;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @JsonIgnore
    private Laptop laptop;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = LicenseStatus.ACTIVE;
        }
        if (licenseType == null) {
            licenseType = LicenseType.PERPETUAL;
        }
    }
} 