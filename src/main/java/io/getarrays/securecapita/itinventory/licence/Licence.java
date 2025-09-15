package io.getarrays.securecapita.itinventory.licence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itinventory.Laptop;
import io.getarrays.securecapita.itinventory.validation.LaptopConditionalValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

// @Entity - Commented out to avoid JPA entity name conflict
// @Table(name = "licence")
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
public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date purchaseDate;




    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date installationDate;



    @Size(max = 100, message = "Manufacturer name must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100, nullable = true)
    private String supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @JsonIgnore
    private Laptop laptop;




}
