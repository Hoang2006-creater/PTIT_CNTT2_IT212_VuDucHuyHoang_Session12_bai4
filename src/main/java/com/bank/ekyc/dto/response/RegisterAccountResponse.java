package com.bank.ekyc.dto.response;

import lombok.*;
import java.util.UUID;

/**
 * Data Transfer Object for account registration output.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterAccountResponse {

    private UUID accountId;
    private String accountNumber;
    private String status;
}
