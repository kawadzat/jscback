package io.getarrays.securecapita.ProjectManagement;

public enum IssueStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    PENDING_ACKNOWLEDGMENT("Pending Acknowledgment"),
    ACKNOWLEDGED("Acknowledged"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String displayName;

    IssueStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}



