package io.getarrays.securecapita.asserts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor

public class Inspection extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;
    @NotNull
    @NotEmpty
    private String remarks;


//    @ManyToOne
//    @JoinColumn(name = "assert_id")
//    //@JoinColumn(name = "assert_entity_id")
//    private AssertEntity assertEntity;


    @ManyToOne
    @JoinColumn(name = "assert_id", nullable = false)
    @JsonBackReference
    private AssertEntity assertEntity;


}
