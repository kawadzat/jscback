package io.getarrays.securecapita.itinventory.IT.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "ITAssert")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class ITAssert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Timestamp date;
    @NotNull
    private String assetDisc;
    @NotNull
    private String assetNumber;
    @NotNull
    private  int quantity;
    @NotNull
    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean movable;

    @NotNull
    private String serialNumber;
    //add all details, station, and only can filter from frontend
    //CAN WE DO IT task filters?
    @NotNull
    private String invoiceNumber;
    @NotNull
    private String assertType;
    @NotNull
    private String location;

    @ManyToOne
    private OfficeLocation officeLocation;

    @NotNull
    private String initialRemarks;//new /or used another fied we left

    // Removed inspections relationship as it's not used and causes JPA mapping conflicts
    // Inspections are handled by AssertEntity, not ITAssert

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "station_id")
    private Station station;

    @Transient
    private Long selectedStationID;

    @ManyToOne
    private User preparedBy;

    @ManyToOne
    private User checkedBy;

    // Removed itServices relationship as it's not used and causes JPA mapping conflicts
    // ITServices are handled by AssertEntity, not ITAssert

    @ManyToMany
    @JoinTable(name = "itassert_assigned_users", joinColumns = @JoinColumn(name = "itassert_id"), inverseJoinColumns =
    @JoinColumn(name = "user_id"))
    private Set<User> assignedUsers = new HashSet<>();



}
