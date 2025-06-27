package io.getarrays.securecapita.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodayOrFutureValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodayOrFuture {
    String message() default "startDate must be today or a future date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
