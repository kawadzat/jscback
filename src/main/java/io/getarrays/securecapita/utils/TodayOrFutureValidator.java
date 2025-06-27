package io.getarrays.securecapita.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class TodayOrFutureValidator implements ConstraintValidator<TodayOrFuture, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) return false;

        Calendar inputCal = Calendar.getInstance();
        inputCal.setTime(value);

        Calendar todayCal = Calendar.getInstance();
        // Set time to start of day (00:00:00)
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        return !inputCal.before(todayCal); // returns true if the date is today or future
    }
}
