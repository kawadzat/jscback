package io.getarrays.securecapita.task;

/**
 * Enum representing different types of tasks
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
public enum TaskTypeEnum {

    /**
     * Repetitive tasks that occur regularly
     */
    REPETITIVE("Repetitive"),
    
    /**
     * Non-repetitive tasks that occur once or irregularly
     */
    NON_REPETITIVE("Non-Repetitive");

    private final String displayName;

    TaskTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name for the task type
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get task type by display name
     * 
     * @param displayName the display name to search for
     * @return the matching TaskTypeEnum or null if not found
     */
    public static TaskTypeEnum fromDisplayName(String displayName) {
        for (TaskTypeEnum taskType : values()) {
            if (taskType.displayName.equalsIgnoreCase(displayName)) {
                return taskType;
            }
        }
        return null;
    }

    /**
     * Check if the task type is repetitive
     * 
     * @return true if repetitive, false otherwise
     */
    public boolean isRepetitive() {
        return this == REPETITIVE;
    }

    /**
     * Check if the task type is non-repetitive
     * 
     * @return true if non-repetitive, false otherwise
     */
    public boolean isNonRepetitive() {
        return this == NON_REPETITIVE;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
