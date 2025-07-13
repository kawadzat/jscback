package io.getarrays.securecapita.bills;

public enum WaterBillStatus {
    PENDING("Pending"),
    SENT("Sent"),
    DELIVERED("Delivered"),
    PAID("Paid"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled");

    private final String displayName;

    WaterBillStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 