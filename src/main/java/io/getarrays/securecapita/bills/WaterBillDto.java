package io.getarrays.securecapita.bills;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterBillDto {

    private Long id;

    @NotBlank(message = "Bill number is required")
    @Pattern(regexp = "^WB\\d{14}[A-Z0-9]{8}$", message = "Invalid bill number format")
    private String billNumber;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String customerPhone;

    @NotBlank(message = "WhatsApp number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid WhatsApp number format")
    private String whatsappNumber;

    @NotBlank(message = "Home address is required")
    @Size(min = 10, max = 500, message = "Home address must be between 10 and 500 characters")
    private String homeAddress;

    @NotBlank(message = "Meter number is required")
    @Size(min = 5, max = 50, message = "Meter number must be between 5 and 50 characters")
    private String meterNumber;

    @NotNull(message = "Billing period start date is required")
    @PastOrPresent(message = "Billing period start date cannot be in the future")
    private LocalDate billingPeriodStart;

    @NotNull(message = "Billing period end date is required")
    @FutureOrPresent(message = "Billing period end date cannot be in the past")
    private LocalDate billingPeriodEnd;

    @NotNull(message = "Previous reading is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Previous reading must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Previous reading must have at most 10 digits and 2 decimal places")
    private BigDecimal previousReading;

    @NotNull(message = "Current reading is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current reading must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Current reading must have at most 10 digits and 2 decimal places")
    private BigDecimal currentReading;

    @NotNull(message = "Rate per unit is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Rate per unit must be greater than 0")
    @Digits(integer = 5, fraction = 2, message = "Rate per unit must have at most 5 digits and 2 decimal places")
    private BigDecimal ratePerUnit;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tax must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Tax must have at most 8 digits and 2 decimal places")
    private BigDecimal tax = BigDecimal.ZERO;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private WaterBillStatus status = WaterBillStatus.PENDING;

    private DeliveryMethod deliveryMethod = DeliveryMethod.WHATSAPP;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    // Validation methods
    public boolean isValidBillingPeriod() {
        return billingPeriodStart != null && billingPeriodEnd != null && 
               !billingPeriodStart.isAfter(billingPeriodEnd);
    }

    public boolean isValidReadings() {
        return previousReading != null && currentReading != null && 
               currentReading.compareTo(previousReading) >= 0;
    }

    public boolean isValidDueDate() {
        return dueDate != null && billingPeriodEnd != null && 
               !dueDate.isBefore(billingPeriodEnd);
    }

    public BigDecimal getConsumption() {
        if (currentReading != null && previousReading != null) {
            return currentReading.subtract(previousReading);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSubtotal() {
        BigDecimal consumption = getConsumption();
        if (consumption != null && ratePerUnit != null) {
            return consumption.multiply(ratePerUnit);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal subtotal = getSubtotal();
        if (subtotal != null && tax != null) {
            return subtotal.add(tax);
        }
        return subtotal != null ? subtotal : BigDecimal.ZERO;
    }
} 