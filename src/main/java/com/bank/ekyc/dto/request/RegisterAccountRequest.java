package com.bank.ekyc.dto.request;

import com.bank.ekyc.validation.CitizenIdConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Data Transfer Object for validating and holding account registration details.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterAccountRequest {

    @NotBlank(message = "fullName must not be blank")
    private String fullName;

    @NotBlank(message = "phone must not be blank")
    private String phone;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be in valid email format")
    private String email;

    @NotBlank(message = "citizenId must not be blank")
    @CitizenIdConstraint(message = "citizenId must be exactly 12 numeric digits")
    private String citizenId;
}
