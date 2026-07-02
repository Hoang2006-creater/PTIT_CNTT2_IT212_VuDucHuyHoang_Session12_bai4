package com.bank.ekyc.exception;

/**
 * Exception thrown when a requested bank account is not found in the system.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Constructs a new AccountNotFoundException with the specified detail message.
     *
     * @param message the detail error message
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
