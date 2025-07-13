package io.getarrays.securecapita.task.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure that repetitive tasks have time interval information
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RepetitiveTaskTimeIntervalValidator.class)
@Documented
public @interface RepetitiveTaskTimeInterval {
    String message() default "Time interval is required for repetitive tasks";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 