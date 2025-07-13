package io.getarrays.securecapita.ProjectManagement;

public enum MilestoneStatus {
    PLANNED("Planned"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    DELAYED("Delayed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    MilestoneStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 