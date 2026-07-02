package com.bank.ekyc.controller;

import com.bank.ekyc.dto.request.RegisterAccountRequest;
import com.bank.ekyc.dto.response.RegisterAccountResponse;
import com.bank.ekyc.exception.DuplicateCitizenIdException;
import com.bank.ekyc.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * REST Controller Integration Test class for {@link AccountController} using JUnit 5 and MockMvc.
 * Mocks out the service dependency to test HTTP responses, validation constraints, and exception handling.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test successful registration HTTP mapping.
     * Expects HTTP status 201 Created and correct response JSON structure.
     */
    @Test
    public void testRegisterAccount_Success() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0987654321")
                .email("vana@gmail.com")
                .citizenId("012345678901")
                .build();

        RegisterAccountResponse expectedResponse = RegisterAccountResponse.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("5812395642")
                .status("PENDING")
                .build();

        // Comment: Giả lập Service phản hồi DTO thành công
        when(accountService.registerAccount(any(RegisterAccountRequest.class))).thenReturn(expectedResponse);

        // Comment: Gọi API POST, kỳ vọng trả về mã HTTP 201 Created và dữ liệu phản hồi đúng
        mockMvc.perform(post("/api/accounts/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId", notNullValue()))
                .andExpect(jsonPath("$.accountNumber", is("5812395642")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    /**
     * Test HTTP mapping when citizenId already exists.
     * Expects HTTP status 409 Conflict.
     */
    @Test
    public void testRegisterAccount_DuplicateCitizenId() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0987654321")
                .email("vana@gmail.com")
                .citizenId("012345678901")
                .build();

        // Comment: Giả lập Service ném lỗi DuplicateCitizenIdException
        when(accountService.registerAccount(any(RegisterAccountRequest.class)))
                .thenThrow(new DuplicateCitizenIdException("Citizen ID 012345678901 already exists in the system."));

        // Comment: Gọi API POST, kỳ vọng trả về mã HTTP 409 Conflict cùng message lỗi tương ứng
        mockMvc.perform(post("/api/accounts/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    /**
     * Test input validation rule for invalid Email format.
     * Expects HTTP status 400 Bad Request.
     */
    @Test
    public void testRegisterAccount_InvalidEmailFormat() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0987654321")
                .email("vana_invalid_email") // Định dạng Email sai
                .citizenId("012345678901")
                .build();

        // Comment: Thực thi request, Spring Validation sẽ tự động chặn và trả về lỗi 400 Bad Request
        mockMvc.perform(post("/api/accounts/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email", containsString("email must be in valid email format")));
    }

    /**
     * Test input validation rule for invalid Citizen ID format (less than 12 digits).
     * Expects HTTP status 400 Bad Request.
     */
    @Test
    public void testRegisterAccount_InvalidCitizenIdFormat() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .fullName("Nguyen Van A")
                .phone("0987654321")
                .email("vana@gmail.com")
                .citizenId("123456") // Số CCCD sai (chỉ có 6 chữ số)
                .build();

        // Comment: Thực thi request, hệ thống bắt lỗi định dạng và trả về HTTP 400 Bad Request
        mockMvc.perform(post("/api/accounts/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.citizenId", containsString("Citizen ID must be exactly 12 numeric digits")));
    }
}
