package io.getarrays.securecapita.ProjectManagement;

public enum ProjectPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical"),
    URGENT("Urgent");

    private final String displayName;

    ProjectPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 