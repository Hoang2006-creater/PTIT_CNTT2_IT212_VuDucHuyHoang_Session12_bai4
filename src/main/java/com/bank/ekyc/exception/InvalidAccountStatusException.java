package com.bank.ekyc.exception;

/**
 * Exception thrown when an invalid account status transition is attempted,
 * or when status update validation fails.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
public class InvalidAccountStatusException extends RuntimeException {

    /**
     * Constructs a new InvalidAccountStatusException with the specified detail message.
     *
     * @param message the detail error message
     */
    public InvalidAccountStatusException(String message) {
        super(message);
    }
}
