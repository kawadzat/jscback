package io.getarrays.securecapita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class AdminPurchaseAuthorization {

    private Long id;
    private String RequestingDepartment;
    private String Station;
    private Date date;
    private  String Reason;
    private BigDecimal budgetLine;
    private String description;
    private int quantity;
    private  BigDecimal  Unitprice;
     private BigDecimal EstimatedValue;
     private  String comments;




}
