package io.getarrays.securecapita.ProjectManagement;

public enum IssuePriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical"),
    URGENT("Urgent");

    private final String displayName;

    IssuePriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}



