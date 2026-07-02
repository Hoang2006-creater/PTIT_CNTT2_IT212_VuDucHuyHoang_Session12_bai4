package com.bank.ekyc.service.impl;

import com.bank.ekyc.dto.request.RegisterAccountRequest;
import com.bank.ekyc.dto.request.UpdateAccountStatusRequest;
import com.bank.ekyc.dto.response.RegisterAccountResponse;
import com.bank.ekyc.dto.response.AccountStatusResponse;
import com.bank.ekyc.entity.Account;
import com.bank.ekyc.exception.DuplicateCitizenIdException;
import com.bank.ekyc.exception.AccountNotFoundException;
import com.bank.ekyc.exception.InvalidAccountStatusException;
import com.bank.ekyc.repository.AccountRepository;
import com.bank.ekyc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Service Implementation class of {@link AccountService} containing business workflow.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Dependency injection via Constructor.
     *
     * @param accountRepository repository layer dependency
     */
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public RegisterAccountResponse registerAccount(RegisterAccountRequest request) {
        // Comment: 1. Kiểm tra sự tồn tại của Số Căn cước công dân (citizenId) trong hệ thống
        if (accountRepository.existsByCitizenId(request.getCitizenId())) {
            // Comment: Ném ra lỗi Conflict (409) nếu số CCCD đã được đăng ký trước đó
            throw new DuplicateCitizenIdException("Citizen ID " + request.getCitizenId() + " already exists in the system.");
        }

        // Comment: 2. Sinh định danh tài khoản ngẫu nhiên sử dụng UUID
        UUID newAccountId = UUID.randomUUID();

        // Comment: 3. Sinh số tài khoản ngân hàng gồm 10 chữ số ngẫu nhiên độc nhất
        String newAccountNumber = generateUniqueAccountNumber();

        // Comment: 4. Chuyển đổi dữ liệu từ Request DTO sang Account Entity
        Account account = Account.builder()
                .accountId(newAccountId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .citizenId(request.getCitizenId())
                .accountNumber(newAccountNumber)
                .status("PENDING") // Comment: Mặc định trạng thái tài khoản là PENDING
                .build();

        // Comment: 5. Lưu thực thể tài khoản vào cơ sở dữ liệu qua Repository
        Account savedAccount = accountRepository.save(account);

        // Comment: 6. Ánh xạ từ Thực thể đã lưu thành DTO kết quả trả về cho Client
        return RegisterAccountResponse.builder()
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .status(savedAccount.getStatus())
                .build();
    }

    @Override
    @Transactional
    public AccountStatusResponse updateAccountStatus(UUID accountId, UpdateAccountStatusRequest request) {
        // Comment: 1. Tìm tài khoản trong cơ sở dữ liệu, nếu không thấy thì ném lỗi 404
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found."));

        // Comment: 2. Kiểm tra xem tài khoản hiện tại có đang ở trạng thái PENDING không
        if (!"PENDING".equalsIgnoreCase(account.getStatus())) {
            throw new InvalidAccountStatusException("Cannot update status. Account is already in " + account.getStatus() + " status.");
        }

        String targetStatus = request.getStatus().toUpperCase();

        // Comment: 3. Nếu chuyển trạng thái sang REJECTED thì bắt buộc phải nhập lý do (reason)
        if ("REJECTED".equals(targetStatus)) {
            if (request.getReason() == null || request.getReason().trim().isEmpty()) {
                throw new InvalidAccountStatusException("Reason must not be blank when status is REJECTED.");
            }
            account.setRejectReason(request.getReason().trim());
        } else {
            // Comment: Nếu là ACTIVE thì reset lý do từ chối về null
            account.setRejectReason(null);
        }

        // Comment: 4. Cập nhật trạng thái mới cho thực thể tài khoản
        account.setStatus(targetStatus);

        // Comment: 5. Lưu thực thể đã cập nhật thông tin xuống DB
        Account updatedAccount = accountRepository.save(account);

        // Comment: 6. Trả về DTO kết quả cập nhật trạng thái
        return AccountStatusResponse.builder()
                .accountId(updatedAccount.getAccountId())
                .accountNumber(updatedAccount.getAccountNumber())
                .status(updatedAccount.getStatus())
                .rejectReason(updatedAccount.getRejectReason())
                .build();
    }

    /**
     * Helper method to generate a unique 10-digit account number.
     * Ensures uniqueness in the database.
     *
     * @return a unique 10-digit account number string
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            // Comment: Sinh một số ngẫu nhiên trong khoảng từ 1.000.000.000 đến 9.999.999.999
            long number = 1000000000L + (long) (secureRandom.nextDouble() * 9000000000L);
            accountNumber = String.valueOf(number);
            // Comment: Lặp lại nếu số tài khoản này trùng lặp với số đã tồn tại trong DB
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        return accountNumber;
    }
}
