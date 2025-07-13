package io.getarrays.securecapita.task;

/**
 * Enum representing time interval units for repetitive tasks
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
public enum TimeIntervalUnit {

    /**
     * Daily repetition
     */
    DAY("Day", "Daily"),
    
    /**
     * Weekly repetition
     */
    WEEK("Week", "Weekly"),
    
    /**
     * Monthly repetition
     */
    MONTH("Month", "Monthly"),
    
    /**
     * Quarterly repetition
     */
    QUARTER("Quarter", "Quarterly"),
    
    /**
     * Yearly repetition
     */
    YEAR("Year", "Yearly");

    private final String displayName;
    private final String frequencyName;

    TimeIntervalUnit(String displayName, String frequencyName) {
        this.displayName = displayName;
        this.frequencyName = frequencyName;
    }

    /**
     * Get the display name for the time interval unit
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the frequency name for the time interval unit
     * 
     * @return the frequency name
     */
    public String getFrequencyName() {
        return frequencyName;
    }

    /**
     * Get time interval unit by display name
     * 
     * @param displayName the display name to search for
     * @return the matching TimeIntervalUnit or null if not found
     */
    public static TimeIntervalUnit fromDisplayName(String displayName) {
        for (TimeIntervalUnit unit : values()) {
            if (unit.displayName.equalsIgnoreCase(displayName)) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Get time interval unit by frequency name
     * 
     * @param frequencyName the frequency name to search for
     * @return the matching TimeIntervalUnit or null if not found
     */
    public static TimeIntervalUnit fromFrequencyName(String frequencyName) {
        for (TimeIntervalUnit unit : values()) {
            if (unit.frequencyName.equalsIgnoreCase(frequencyName)) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 