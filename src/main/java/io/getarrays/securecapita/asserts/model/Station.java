package io.getarrays.securecapita.asserts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@EqualsAndHashCode
@Getter
@Setter


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "station")
public class Station {

    @Column(name = "station_id", columnDefinition = "BIGINT default 0")
    @Id
    private Long station_id;
    @ManyToOne
    private Province province;
    @Column(name = "station_name")
    private String stationName;

    @OneToMany(mappedBy = "station")
    @Builder.Default
    @JsonIgnore
    private Set<AssertEntity> asserts = new HashSet<>();

    @OneToMany(mappedBy = "station")
    @Builder.Default
    @JsonIgnore
    private Set<OfficeLocation> locations = new HashSet<>();

    public void add(AssertEntity assertEntity) {
        asserts.add(assertEntity);
    }
}
