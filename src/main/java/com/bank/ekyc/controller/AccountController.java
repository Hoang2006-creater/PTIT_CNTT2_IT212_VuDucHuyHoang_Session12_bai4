package com.bank.ekyc.controller;

import com.bank.ekyc.dto.request.RegisterAccountRequest;
import com.bank.ekyc.dto.request.UpdateAccountStatusRequest;
import com.bank.ekyc.dto.response.RegisterAccountResponse;
import com.bank.ekyc.dto.response.AccountStatusResponse;
import com.bank.ekyc.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller providing APIs for eKYC account onboarding.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * Dependency injection via Constructor.
     *
     * @param accountService service layer dependency
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Registers a new customer bank account.
     * Validates input request and returns created status (201).
     *
     * @param request the validated registration request
     * @return ResponseEntity enclosing registration response data
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterAccountResponse> registerAccount(@Valid @RequestBody RegisterAccountRequest request) {
        // Comment: Thực hiện gọi tầng Service để xử lý logic nghiệp vụ đăng ký mở tài khoản
        RegisterAccountResponse response = accountService.registerAccount(request);

        // Comment: Trả về mã phản hồi HTTP 201 Created cùng kết quả đối tượng Response DTO
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates status of an account (Approve/Reject).
     * Validates status input and returns OK status (200).
     *
     * @param accountId the unique account identifier
     * @param request the status update request DTO
     * @return ResponseEntity enclosing updated account status details
     */
    @PutMapping("/{accountId}/status")
    public ResponseEntity<AccountStatusResponse> updateAccountStatus(
            @PathVariable UUID accountId,
            @Valid @RequestBody UpdateAccountStatusRequest request) {
        // Comment: Thực hiện gọi tầng Service để xử lý logic nghiệp vụ cập nhật trạng thái tài khoản
        AccountStatusResponse response = accountService.updateAccountStatus(accountId, request);

        // Comment: Trả về mã phản hồi HTTP 200 OK cùng kết quả DTO cập nhật
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
