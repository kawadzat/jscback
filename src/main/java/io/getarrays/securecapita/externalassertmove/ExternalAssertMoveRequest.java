package io.getarrays.securecapita.externalassertmove;

import io.getarrays.securecapita.assertmoverequests.AssertMoveStatus;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.officelocations.OfficeLocation;
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
@Table(name = "assert_move_requests")
public class ExternalAssertMoveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private OfficeLocation officeLocation;

    @ManyToOne
    private AssertEntity assertEntity;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    @Enumerated(EnumType.STRING)
    private AssertMoveStatus status;

}
