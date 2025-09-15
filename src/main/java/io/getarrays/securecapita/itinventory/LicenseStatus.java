package io.getarrays.securecapita.itinventory;

/**
 * Enumeration for different statuses of software licenses
 */
public enum LicenseStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    EXPIRED("Expired"),
    SUSPENDED("Suspended"),
    PENDING("Pending"),
    RENEWAL_PENDING("Renewal Pending"),
    CANCELLED("Cancelled"),
    ARCHIVED("Archived"),
    MAINTENANCE("Under Maintenance"),
    TRANSFER_PENDING("Transfer Pending");

    private final String displayName;

    LicenseStatus(String displayName) {
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