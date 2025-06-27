package io.getarrays.securecapita.asserts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
@NamedEntityGraph(name = "assert-entity-graph")
@Table(name = "`assert`")
public class AssertEntity extends Auditable<String> {
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

    @OneToMany(mappedBy = "assertEntity", cascade = ALL)
    @JsonManagedReference
    private List<Inspection> inspections;

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

    @OneToMany(mappedBy = "assertEntity", cascade = ALL)
    @JsonManagedReference
    private List< ITServices> itServices;

    @ManyToMany
    @JoinTable(name = "assert_assigned_users", joinColumns = @JoinColumn(name = "assert_id"), inverseJoinColumns =
    @JoinColumn(name = "user_id"))
    private Set<User> assignedUsers = new HashSet<>();


}
