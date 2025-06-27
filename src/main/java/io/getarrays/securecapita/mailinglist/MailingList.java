package io.getarrays.securecapita.mailinglist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

public class MailingList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Timestamp date;





    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "station_id")
    private Station station;


    @Transient
    private Long selectedStationID;


    @ManyToOne
    private User preparedBy;


}
