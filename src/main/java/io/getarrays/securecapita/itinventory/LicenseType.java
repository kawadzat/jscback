package io.getarrays.securecapita.itinventory;

/**
 * Enumeration for different types of software licenses
 */
public enum LicenseType {
    PERPETUAL("Perpetual License"),
    SUBSCRIPTION("Subscription License"),
    CONCURRENT("Concurrent License"),
    FLOATING("Floating License"),
    NAMED_USER("Named User License"),
    DEVICE("Device License"),
    VOLUME("Volume License"),
    ACADEMIC("Academic License"),
    TRIAL("Trial License"),
    OPEN_SOURCE("Open Source License"),
    FREE("Free License"),
    CUSTOM("Custom License");

    private final String displayName;

    LicenseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 