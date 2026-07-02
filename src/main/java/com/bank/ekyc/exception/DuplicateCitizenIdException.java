package com.bank.ekyc.exception;

/**
 * Exception thrown when a registration request contains a citizenId (CCCD)
 * that is already associated with an existing account in the database.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
public class DuplicateCitizenIdException extends RuntimeException {

    /**
     * Constructs a new DuplicateCitizenIdException with the specified error message.
     *
     * @param message the detail error message
     */
    public DuplicateCitizenIdException(String message) {
        super(message);
    }
}
