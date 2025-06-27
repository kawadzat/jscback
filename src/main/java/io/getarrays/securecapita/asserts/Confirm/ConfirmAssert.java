package io.getarrays.securecapita.asserts.Confirm;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
public class ConfirmAssert extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private AssertEntity asset;

    @ManyToOne
    private User confirmedBy;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
