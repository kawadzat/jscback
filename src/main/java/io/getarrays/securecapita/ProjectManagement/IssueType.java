package io.getarrays.securecapita.ProjectManagement;

public enum IssueType {
    BUG("Bug"),
    FEATURE("Feature"),
    TASK("Task"),
    STORY("Story"),
    EPIC("Epic"),
    IMPROVEMENT("Improvement"),
    DOCUMENTATION("Documentation"),
    TEST("Test"),
    MAINTENANCE("Maintenance"),
    OTHER("Other");

    private final String displayName;

    IssueType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}



