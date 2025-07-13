package io.getarrays.securecapita.task;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for TaskTypeEnum operations
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
public class TaskTypeUtils {

    /**
     * Get all task types as a list
     * 
     * @return List of all TaskTypeEnum values
     */
    public static List<TaskTypeEnum> getAllTaskTypes() {
        return Arrays.asList(TaskTypeEnum.values());
    }

    /**
     * Get all task type display names as a list
     * 
     * @return List of all task type display names
     */
    public static List<String> getAllTaskTypeDisplayNames() {
        return Arrays.stream(TaskTypeEnum.values())
                .map(TaskTypeEnum::getDisplayName)
                .collect(Collectors.toList());
    }

    /**
     * Get repetitive task types
     * 
     * @return List of repetitive task types
     */
    public static List<TaskTypeEnum> getRepetitiveTaskTypes() {
        return Arrays.stream(TaskTypeEnum.values())
                .filter(TaskTypeEnum::isRepetitive)
                .collect(Collectors.toList());
    }

    /**
     * Get non-repetitive task types
     * 
     * @return List of non-repetitive task types
     */
    public static List<TaskTypeEnum> getNonRepetitiveTaskTypes() {
        return Arrays.stream(TaskTypeEnum.values())
                .filter(TaskTypeEnum::isNonRepetitive)
                .collect(Collectors.toList());
    }

    /**
     * Check if a task type is valid
     * 
     * @param taskType the task type to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidTaskType(String taskType) {
        try {
            TaskTypeEnum.valueOf(taskType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get task type by name (case-insensitive)
     * 
     * @param taskTypeName the task type name
     * @return the TaskTypeEnum or null if not found
     */
    public static TaskTypeEnum getTaskTypeByName(String taskTypeName) {
        try {
            return TaskTypeEnum.valueOf(taskTypeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get task type by display name (case-insensitive)
     * 
     * @param displayName the display name
     * @return the TaskTypeEnum or null if not found
     */
    public static TaskTypeEnum getTaskTypeByDisplayName(String displayName) {
        return TaskTypeEnum.fromDisplayName(displayName);
    }
} 