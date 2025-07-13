package io.getarrays.securecapita.bills;

public enum DeliveryMethod {
    WHATSAPP("WhatsApp"),
    EMAIL("Email"),
    SMS("SMS"),
    POSTAL_MAIL("Postal Mail"),
    HAND_DELIVERY("Hand Delivery");

    private final String displayName;

    DeliveryMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 