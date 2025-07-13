package io.getarrays.securecapita.task;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for TimeIntervalUnit operations
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
public class TimeIntervalUtils {

    /**
     * Get all time interval units as a list
     * 
     * @return List of all TimeIntervalUnit values
     */
    public static List<TimeIntervalUnit> getAllTimeIntervalUnits() {
        return Arrays.asList(TimeIntervalUnit.values());
    }

    /**
     * Get all time interval unit display names as a list
     * 
     * @return List of all time interval unit display names
     */
    public static List<String> getAllTimeIntervalUnitDisplayNames() {
        return Arrays.stream(TimeIntervalUnit.values())
                .map(TimeIntervalUnit::getDisplayName)
                .collect(Collectors.toList());
    }

    /**
     * Get all time interval unit frequency names as a list
     * 
     * @return List of all time interval unit frequency names
     */
    public static List<String> getAllTimeIntervalUnitFrequencyNames() {
        return Arrays.stream(TimeIntervalUnit.values())
                .map(TimeIntervalUnit::getFrequencyName)
                .collect(Collectors.toList());
    }

    /**
     * Check if a time interval unit is valid
     * 
     * @param timeIntervalUnit the time interval unit to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidTimeIntervalUnit(String timeIntervalUnit) {
        try {
            TimeIntervalUnit.valueOf(timeIntervalUnit);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get time interval unit by name (case-insensitive)
     * 
     * @param timeIntervalUnitName the time interval unit name
     * @return the TimeIntervalUnit or null if not found
     */
    public static TimeIntervalUnit getTimeIntervalUnitByName(String timeIntervalUnitName) {
        try {
            return TimeIntervalUnit.valueOf(timeIntervalUnitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get time interval unit by display name (case-insensitive)
     * 
     * @param displayName the display name
     * @return the TimeIntervalUnit or null if not found
     */
    public static TimeIntervalUnit getTimeIntervalUnitByDisplayName(String displayName) {
        return TimeIntervalUnit.fromDisplayName(displayName);
    }

    /**
     * Get time interval unit by frequency name (case-insensitive)
     * 
     * @param frequencyName the frequency name
     * @return the TimeIntervalUnit or null if not found
     */
    public static TimeIntervalUnit getTimeIntervalUnitByFrequencyName(String frequencyName) {
        return TimeIntervalUnit.fromFrequencyName(frequencyName);
    }

    /**
     * Get time interval units suitable for short-term tasks
     * 
     * @return List of short-term time interval units
     */
    public static List<TimeIntervalUnit> getShortTermTimeIntervalUnits() {
        return Arrays.asList(TimeIntervalUnit.DAY, TimeIntervalUnit.WEEK);
    }

    /**
     * Get time interval units suitable for long-term tasks
     * 
     * @return List of long-term time interval units
     */
    public static List<TimeIntervalUnit> getLongTermTimeIntervalUnits() {
        return Arrays.asList(TimeIntervalUnit.MONTH, TimeIntervalUnit.QUARTER, TimeIntervalUnit.YEAR);
    }
} 