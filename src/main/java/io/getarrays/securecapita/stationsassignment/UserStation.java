package io.getarrays.securecapita.stationsassignment;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_stations")
public class UserStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private User assignedBy;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private Timestamp createdDate;
}
