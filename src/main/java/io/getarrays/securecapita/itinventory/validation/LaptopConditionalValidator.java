package io.getarrays.securecapita.itinventory.validation;

import io.getarrays.securecapita.itinventory.Laptop;
import io.getarrays.securecapita.itinventory.LaptopDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

/**
 * Custom validator for conditional validation of issue date based on asset type
 * - For LAPTOP assets: Issue date cannot be null or zero (required)
 * - For non-LAPTOP assets: Issue date can be null or zero (optional)
 */
public class LaptopConditionalValidator implements ConstraintValidator<LaptopConditionalValidation, Object> {

    @Override
    public void initialize(LaptopConditionalValidation constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true; // Let other validators handle null checks
        }

        String assertType = null;
        Date issueDate = null;

        // Handle LaptopDto
        if (object instanceof LaptopDto) {
            LaptopDto laptopDto = (LaptopDto) object;
            assertType = laptopDto.getAssertType();
            issueDate = laptopDto.getIssueDate();
        }
        // Handle Laptop entity
        else if (object instanceof Laptop) {
            Laptop laptop = (Laptop) object;
            assertType = laptop.getAssertType();
            issueDate = laptop.getIssueDate();
        }
        // Unknown type
        else {
            return true; // Skip validation for unknown types
        }

        // Check if this is a LAPTOP asset
        boolean isLaptop = "LAPTOP".equalsIgnoreCase(assertType);

        // For LAPTOP assets, issue date cannot be null or zero
        if (isLaptop) {
            if (issueDate == null) {
                addConstraintViolation(context, "Issue date cannot be null for LAPTOP assets");
                return false;
            }
            
            // Check if the date is effectively "zero" (epoch time or very early date)
            if (issueDate.getTime() <= 0 || issueDate.getTime() < 1000000000000L) { // Before year 2001
                addConstraintViolation(context, "Issue date cannot be zero or invalid for LAPTOP assets");
                return false;
            }
        }

        // For non-LAPTOP assets, issue date can be null or zero (optional)
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
    }
} 