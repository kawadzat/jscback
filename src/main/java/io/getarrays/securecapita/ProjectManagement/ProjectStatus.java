package io.getarrays.securecapita.ProjectManagement;

public enum ProjectStatus {
    PLANNING("Planning"),
    ACTIVE("Active"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    SUSPENDED("Suspended"),
    REVIEW("Under Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    ARCHIVED("Archived");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 