package io.getarrays.securecapita.meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
@Table(name = "`meeting`")
public class Meeting {     @Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id; private LocalDate meetingDate;       // Format: dd/mm/yyyy
    private LocalTime time;              // Format: --:--
    private String agenda;                // Agent name
    private String place;                // Meeting place
    private List<String> attendeesEmail;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;



}
