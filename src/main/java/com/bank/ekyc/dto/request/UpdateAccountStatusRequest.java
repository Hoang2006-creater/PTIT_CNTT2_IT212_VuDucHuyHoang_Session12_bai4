package com.bank.ekyc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Data Transfer Object for Update Account Status Request.
 * Used by Back-office operator to approve or reject eKYC.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountStatusRequest {

    @NotBlank(message = "status must not be blank")
    @Pattern(regexp = "^(ACTIVE|REJECTED)$", message = "status must be either ACTIVE or REJECTED")
    private String status;

    private String reason;
}
