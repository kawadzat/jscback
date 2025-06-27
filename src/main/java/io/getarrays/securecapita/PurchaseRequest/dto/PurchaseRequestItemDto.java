package io.getarrays.securecapita.PurchaseRequest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class PurchaseRequestItemDto {
    private Long id;

    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String ref;

    @Min(value = 1, message = "Number must be greater than or equal to 1")
    private int number;


    private String description;

    @NotNull(message = "Unit Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit Price must be greater than 0")
    private Double unitPrice;

    @NotNull(message = "quantity cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit Price must be greater than 0")
    private Double quantity;

    @NotNull(message = "Estimated Value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated Value must be greater than 0")
    private Double estimatedValue;
}
