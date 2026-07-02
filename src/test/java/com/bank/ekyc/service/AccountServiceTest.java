package com.bank.ekyc.service;

import com.bank.ekyc.dto.request.RegisterAccountRequest;
import com.bank.ekyc.dto.response.RegisterAccountResponse;
import com.bank.ekyc.entity.Account;
import com.bank.ekyc.exception.DuplicateCitizenIdException;
import com.bank.ekyc.repository.AccountRepository;
import com.bank.ekyc.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test class for {@link AccountServiceImpl} using JUnit 5 and Mockito.
 * Tests business rules, registration workflows, exception conditions, and repository errors.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private RegisterAccountRequest request;

    /**
     * Initializes a sample RegisterAccountRequest before running each test.
     */
    @BeforeEach
    public void setup() {
        request = RegisterAccountRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0987654321")
                .email("vana@gmail.com")
                .citizenId("012345678901")
                .build();
    }

    /**
     * Test case to verify successful registration.
     * Mocks repository exists checks and save methods, then asserts output values.
     */
    @Test
    public void testRegisterAccount_Success() {
        // Comment: Giả lập CSDL chưa có Số CCCD này và số tài khoản này
        when(accountRepository.existsByCitizenId(request.getCitizenId())).thenReturn(false);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);

        // Comment: Giả lập lưu thành công và trả về chính thực thể tài khoản đã xây dựng
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account accountToSave = invocation.getArgument(0);
            return accountToSave;
        });

        // Comment: Gọi phương thức nghiệp vụ đăng ký
        RegisterAccountResponse response = accountService.registerAccount(request);

        // Comment: Kiểm chứng tính đúng đắn của dữ liệu đầu ra (Response)
        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertEquals(10, response.getAccountNumber().length());
        assertEquals("PENDING", response.getStatus());

        // Comment: Xác nhận các Mock method được gọi đúng số lần mong muốn
        verify(accountRepository, times(1)).existsByCitizenId(request.getCitizenId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    /**
     * Test case to verify DuplicateCitizenIdException is thrown when citizenId already exists.
     */
    @Test
    public void testRegisterAccount_DuplicateCitizenId() {
        // Comment: Giả lập Số CCCD đã tồn tại trong CSDL
        when(accountRepository.existsByCitizenId(request.getCitizenId())).thenReturn(true);

        // Comment: Xác thực ngoại lệ DuplicateCitizenIdException được ném ra
        DuplicateCitizenIdException exception = assertThrows(DuplicateCitizenIdException.class, () -> {
            accountService.registerAccount(request);
        });

        // Comment: Kiểm tra thông báo lỗi
        assertTrue(exception.getMessage().contains("already exists"));
        
        // Comment: Đảm bảo CSDL không được gọi lưu trữ
        verify(accountRepository, never()).save(any(Account.class));
    }

    /**
     * Test case to verify exception propagation when database repository encounters an error.
     */
    @Test
    public void testRegisterAccount_RepositoryError() {
        // Comment: Giả lập lỗi kết nối CSDL khi gọi lưu trữ
        when(accountRepository.existsByCitizenId(request.getCitizenId())).thenReturn(false);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("Database connection failure"));

        // Comment: Xác thực ngoại lệ RuntimeException ném ngược lại từ Service
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.registerAccount(request);
        });

        assertEquals("Database connection failure", exception.getMessage());
    }
}
