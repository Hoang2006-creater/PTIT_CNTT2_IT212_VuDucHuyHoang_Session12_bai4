package com.bank.ekyc.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for {@link CitizenIdConstraint}.
 * Checks if the string value contains exactly 12 numeric digits.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
public class CitizenIdValidator implements ConstraintValidator<CitizenIdConstraint, String> {

    @Override
    public void initialize(CitizenIdConstraint constraintAnnotation) {
        // Comment: Khởi tạo validator (Không yêu cầu xử lý logic đặc biệt)
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Comment: Nếu giá trị đầu vào là null, ta để các ràng buộc khác như @NotBlank xử lý
        if (value == null) {
            return true;
        }
        // Comment: Kiểm tra xem chuỗi đầu vào có khớp với regex chỉ gồm 12 chữ số hay không
        return value.matches("^\\d{12}$");
    }
}
