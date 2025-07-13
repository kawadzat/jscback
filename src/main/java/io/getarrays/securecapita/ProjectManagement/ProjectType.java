package io.getarrays.securecapita.ProjectManagement;

public enum ProjectType {
    DEVELOPMENT("Development"),
    MAINTENANCE("Maintenance"),
    RESEARCH("Research"),
    INFRASTRUCTURE("Infrastructure"),
    MIGRATION("Migration"),
    UPGRADE("Upgrade"),
    CONSULTING("Consulting"),
    TRAINING("Training"),
    DOCUMENTATION("Documentation"),
    TESTING("Testing"),
    DEPLOYMENT("Deployment"),
    INTEGRATION("Integration"),
    CUSTOMIZATION("Customization"),
    SUPPORT("Support"),
    OTHER("Other");

    private final String displayName;

    ProjectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 