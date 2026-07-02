package com.bank.ekyc.service;

import com.bank.ekyc.dto.request.RegisterAccountRequest;
import com.bank.ekyc.dto.request.UpdateAccountStatusRequest;
import com.bank.ekyc.dto.response.RegisterAccountResponse;
import com.bank.ekyc.dto.response.AccountStatusResponse;
import java.util.UUID;

/**
 * Service Interface defining the business logic for managing Bank Accounts.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
public interface AccountService {

    /**
     * Registers a new bank account in the system after validating inputs and checking duplicates.
     *
     * @param request the account registration payload
     * @return the registration response containing accountId, accountNumber, and status
     * @throws com.bank.ekyc.exception.DuplicateCitizenIdException if citizenId is already registered
     */
    RegisterAccountResponse registerAccount(RegisterAccountRequest request);

    /**
     * Updates the status of an onboarding account (approve/reject).
     *
     * @param accountId the unique account ID
     * @param request the status update payload
     * @return the updated account status response
     * @throws com.bank.ekyc.exception.AccountNotFoundException if accountId does not exist
     * @throws com.bank.ekyc.exception.InvalidAccountStatusException if validation/transition fails
     */
    AccountStatusResponse updateAccountStatus(UUID accountId, UpdateAccountStatusRequest request);
}
