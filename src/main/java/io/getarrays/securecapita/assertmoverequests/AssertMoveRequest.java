package io.getarrays.securecapita.assertmoverequests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assert_move_requests")
@NamedEntityGraph(name = "assert-move-requests-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("officeLocation"),
                @NamedAttributeNode("assertEntity"),
                @NamedAttributeNode("initiatedBy"),
                @NamedAttributeNode("approvedBy")
        }
)
public class AssertMoveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    private OfficeLocation officeLocation;
    private String reason;
    @ManyToOne
    private AssertEntity assertEntity;
    @ManyToOne
    private User initiatedBy;
    @ManyToOne
    private User approvedBy;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    @Enumerated(EnumType.STRING)
    private AssertMoveStatus status;
}
