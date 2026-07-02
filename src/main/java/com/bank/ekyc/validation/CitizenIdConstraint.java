package com.bank.ekyc.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom annotation for Citizen ID (CCCD) validation.
 * Ensures that the provided field or parameter contains exactly 12 numeric digits.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = CitizenIdValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CitizenIdConstraint {
    
    /**
     * Error message displayed when validation fails.
     *
     * @return the error message
     */
    String message() default "Citizen ID must be exactly 12 numeric digits";
    
    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * @return the groups
     */
    Class<?>[] groups() default {};
    
    /**
     * Can be used by clients of the Jakarta Bean Validation API to assign custom payload objects to a constraint.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}
