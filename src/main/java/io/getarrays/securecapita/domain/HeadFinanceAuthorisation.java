package io.getarrays.securecapita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class HeadFinanceAuthorisation {
    private boolean plannedProcurement;
    private  boolean budgetedProcurement;
    private int     Fundingdays;
    private String name;
    //signature
    private Date date;





}
