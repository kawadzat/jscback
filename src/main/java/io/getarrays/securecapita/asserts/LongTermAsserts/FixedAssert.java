package io.getarrays.securecapita.asserts.LongTermAsserts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
@Table(name = "`FixedAssert`")
@EntityListeners(AuditingEntityListener.class)
public class FixedAssert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String assetDisc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp acquisitionDate;


    private String fixedAssetNumber;

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


}
