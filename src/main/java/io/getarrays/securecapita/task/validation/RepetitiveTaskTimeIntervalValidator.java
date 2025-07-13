package io.getarrays.securecapita.task.validation;

import io.getarrays.securecapita.task.TaskDto;
import io.getarrays.securecapita.task.TaskTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for RepetitiveTaskTimeInterval annotation
 */
public class RepetitiveTaskTimeIntervalValidator implements ConstraintValidator<RepetitiveTaskTimeInterval, TaskDto> {

    @Override
    public void initialize(RepetitiveTaskTimeInterval constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(TaskDto taskDto, ConstraintValidatorContext context) {
        if (taskDto == null || taskDto.getType() == null) {
            return true; // Let other validators handle null checks
        }

        try {
            TaskTypeEnum taskType = TaskTypeEnum.valueOf(taskDto.getType());
            
            // If task is repetitive, time interval is required
            if (taskType == TaskTypeEnum.REPETITIVE) {
                return taskDto.getTimeIntervalValue() != null && 
                       taskDto.getTimeIntervalValue() > 0 && 
                       taskDto.getTimeIntervalUnit() != null && 
                       !taskDto.getTimeIntervalUnit().trim().isEmpty();
            }
            
            // For non-repetitive tasks, time interval is optional
            return true;
        } catch (IllegalArgumentException e) {
            // Invalid task type, let other validators handle this
            return true;
        }
    }
} 