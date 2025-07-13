package io.getarrays.securecapita.bills;

import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerAddress extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String customerWhatsapp;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String apartmentUnit;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String landmark;

    @Column(nullable = false)
    private String addressType; // HOME, BUSINESS, BILLING, SHIPPING

    @Column(nullable = false)
    private Boolean isPrimary = false;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column
    private String notes;

    // Many-to-One relationship with TownAddress
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_address_id", nullable = false)
    private TownAddress townAddress;

    // Validation methods
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+?[1-9]\\d{1,14}$");
    }

    public boolean isValidWhatsAppNumber(String whatsappNumber) {
        return whatsappNumber != null && whatsappNumber.matches("^\\+?[1-9]\\d{1,14}$");
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Utility methods
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s, %s, %s", 
            streetAddress, apartmentUnit, neighborhood, landmark, 
            townAddress.getTownName(), townAddress.getState());
    }

    public String getContactInfo() {
        return String.format("Phone: %s, WhatsApp: %s, Email: %s", 
            customerPhone, customerWhatsapp, customerEmail);
    }
} 