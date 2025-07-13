package io.getarrays.securecapita.ProjectManagement;

public enum TaskStatus {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    TESTING("Testing"),
    DONE("Done"),
    BLOCKED("Blocked"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 