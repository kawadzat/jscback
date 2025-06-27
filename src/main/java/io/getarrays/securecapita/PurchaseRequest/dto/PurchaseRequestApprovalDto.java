package io.getarrays.securecapita.PurchaseRequest.dto;

import io.getarrays.securecapita.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseRequestApprovalDto {
    private Long id;

    private UserDTO user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String signature; // Base64-encoded signature image

    private boolean approve;
}

