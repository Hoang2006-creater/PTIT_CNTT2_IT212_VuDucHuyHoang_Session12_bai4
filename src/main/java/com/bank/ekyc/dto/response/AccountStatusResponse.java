package com.bank.ekyc.dto.response;

import lombok.*;
import java.util.UUID;

/**
 * Data Transfer Object representing the updated status response of an Account.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusResponse {

    private UUID accountId;
    private String accountNumber;
    private String status;
    private String rejectReason;
}
