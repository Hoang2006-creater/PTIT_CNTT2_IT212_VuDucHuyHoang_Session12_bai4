package com.bank.ekyc.repository;

import com.bank.ekyc.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Spring Data JPA Repository interface to perform CRUD and database queries
 * on {@link Account} entity.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Checks if a record exists with the specified citizenId (CCCD).
     * Used for compliance and preventing duplicate registration.
     *
     * @param citizenId the citizen ID to verify
     * @return true if citizen ID already exists, false otherwise
     */
    boolean existsByCitizenId(String citizenId);

    /**
     * Checks if a record exists with the specified accountNumber.
     * Used to ensure unique generation of account numbers.
     *
     * @param accountNumber the account number to verify
     * @return true if account number already exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
}
