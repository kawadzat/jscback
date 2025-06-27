package io.getarrays.securecapita.asserts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
public class ITServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;
    @NotNull
    @NotEmpty
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "assert_id", nullable = false)
    @JsonBackReference
    private AssertEntity assertEntity;


}
