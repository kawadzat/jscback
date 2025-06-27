package io.getarrays.securecapita.officelocations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.getarrays.securecapita.asserts.model.Station;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "office_locations")
public class OfficeLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "station_id")
    private Station station;
}
