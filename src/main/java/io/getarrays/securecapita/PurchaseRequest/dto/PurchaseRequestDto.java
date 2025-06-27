package io.getarrays.securecapita.PurchaseRequest.dto;

import io.getarrays.securecapita.department.dto.DepartmentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseRequestDto {
    private Long id;

    @NotNull(message = "date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String code;

    @NotNull(message = "departmentId must not be null")
    private Long departmentId;

    private DepartmentDto department;

    private String reason;

    @NotEmpty
    @Valid
    private List<PurchaseRequestItemDto> requestItems;

    private String status;

    private List<PurchaseRequestApprovalDto> approvals;

    private String signature;
}

//can you tell me user flow?
//
//every deparment makes purchase request
//each purchase request has products requested
//for starting lets just make a purchase request
// requested for existing products right?
//they just make request
//a person in a department makes a request then forward the request to next person,
//person also forwards the request to last person
//at each stage a person appove or deny the request by signature

