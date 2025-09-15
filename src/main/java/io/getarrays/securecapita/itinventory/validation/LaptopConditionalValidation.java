package io.getarrays.securecapita.itinventory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LaptopConditionalValidator.class)
public @interface LaptopConditionalValidation {
    String message() default "Validation failed for laptop-specific fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 