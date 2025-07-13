package io.getarrays.securecapita.bills;

import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "town_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TownAddress extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "town_name", nullable = false)
    private String townName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, unique = true)
    private String townCode;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String mainOfficePhone;

    @Column(nullable = false)
    private String mainOfficeWhatsapp;

    @Column(nullable = false)
    private String emergencyPhone;

    @Column(nullable = false)
    private String customerServicePhone;

    @Column(nullable = false)
    private String customerServiceWhatsapp;

    @Column(nullable = false)
    private String billingOfficePhone;

    @Column(nullable = false)
    private String billingOfficeWhatsapp;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String language;

    @Column
    private String notes;

    // One-to-Many relationship with customer addresses
    @OneToMany(mappedBy = "townAddress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomerAddress> customerAddresses;

    // One-to-Many relationship with water bills
    @OneToMany(mappedBy = "townAddress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WaterBill> waterBills;

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

    public boolean isValidPostalCode(String postalCode) {
        return postalCode != null && postalCode.length() >= 3 && postalCode.length() <= 10;
    }

    // Utility method to get full address
    public String getFullAddress() {
        return String.format("%s, %s, %s %s, %s", 
            townName, city, state, postalCode, country);
    }

    public String getContactInfo() {
        return String.format("Phone: %s, WhatsApp: %s, Email: %s", 
            mainOfficePhone, mainOfficeWhatsapp, email);
    }

    public String getBillingContactInfo() {
        return String.format("Billing Phone: %s, Billing WhatsApp: %s", 
            billingOfficePhone, billingOfficeWhatsapp);
    }
} 