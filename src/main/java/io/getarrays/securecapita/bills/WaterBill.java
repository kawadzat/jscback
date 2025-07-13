package io.getarrays.securecapita.bills;

import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WaterBill extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String whatsappNumber;

    @Column(nullable = false)
    private String homeAddress;

    @Column(nullable = false)
    private String meterNumber;

    @Column(nullable = false)
    private LocalDate billingPeriodStart;

    @Column(nullable = false)
    private LocalDate billingPeriodEnd;

    @Column(nullable = false)
    private BigDecimal previousReading;

    @Column(nullable = false)
    private BigDecimal currentReading;

    @Column(nullable = false)
    private BigDecimal consumption;

    @Column(nullable = false)
    private BigDecimal ratePerUnit;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal tax;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaterBillStatus status = WaterBillStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryMethod deliveryMethod = DeliveryMethod.WHATSAPP;

    @Column
    private LocalDateTime sentAt;

    @Column
    private String deliveryStatus;

    @Column
    private String deliveryMessage;

    @Column
    private String notes;

    // Many-to-One relationship with TownAddress
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_address_id")
    private TownAddress townAddress;

    // Many-to-One relationship with CustomerAddress (specific address for billing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_address_id")
    private CustomerAddress customerAddress;

    // Calculated fields
    @Transient
    public BigDecimal getConsumption() {
        if (currentReading != null && previousReading != null) {
            return currentReading.subtract(previousReading);
        }
        return BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getSubtotal() {
        if (consumption != null && ratePerUnit != null) {
            return consumption.multiply(ratePerUnit);
        }
        return BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getTotalAmount() {
        if (subtotal != null && tax != null) {
            return subtotal.add(tax);
        }
        return subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    // Get WhatsApp number from customer address if available
    @Transient
    public String getWhatsAppNumberForDelivery() {
        if (customerAddress != null && customerAddress.getCustomerWhatsapp() != null) {
            return customerAddress.getCustomerWhatsapp();
        }
        return whatsappNumber;
    }

    // Get customer phone from customer address if available
    @Transient
    public String getCustomerPhoneForDelivery() {
        if (customerAddress != null && customerAddress.getCustomerPhone() != null) {
            return customerAddress.getCustomerPhone();
        }
        return customerPhone;
    }

    // Get full address from customer address if available
    @Transient
    public String getFullAddressForDelivery() {
        if (customerAddress != null) {
            return customerAddress.getFullAddress();
        }
        return homeAddress;
    }

    @PrePersist
    @PreUpdate
    public void calculateFields() {
        this.consumption = getConsumption();
        this.subtotal = getSubtotal();
        this.totalAmount = getTotalAmount();
    }
} 