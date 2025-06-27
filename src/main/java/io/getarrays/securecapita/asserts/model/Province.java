package io.getarrays.securecapita.asserts.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "province")
public class Province {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    
    @Column(name = "station_id")
    private Long stationId;
}
