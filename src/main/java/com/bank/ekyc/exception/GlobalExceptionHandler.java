package com.bank.ekyc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler that intercepts specific exceptions and constructs
 * standardized JSON error responses.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches and handles {@link DuplicateCitizenIdException}.
     * Returns an HTTP 409 Conflict status with error details.
     *
     * @param ex the caught DuplicateCitizenIdException
     * @return ResponseEntity containing a map of error details
     */
    @ExceptionHandler(DuplicateCitizenIdException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateCitizenIdException(DuplicateCitizenIdException ex) {
        // Comment: Đóng gói thông tin chi tiết về lỗi trùng CCCD (409)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Catches and handles {@link AccountNotFoundException}.
     * Returns an HTTP 404 Not Found status with error details.
     *
     * @param ex the caught AccountNotFoundException
     * @return ResponseEntity containing a map of error details
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFoundException(AccountNotFoundException ex) {
        // Comment: Đóng gói thông tin chi tiết về lỗi không tìm thấy tài khoản (404)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Catches and handles {@link InvalidAccountStatusException}.
     * Returns an HTTP 400 Bad Request status with error details.
     *
     * @param ex the caught InvalidAccountStatusException
     * @return ResponseEntity containing a map of error details
     */
    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAccountStatusException(InvalidAccountStatusException ex) {
        // Comment: Đóng gói thông tin chi tiết về lỗi cập nhật trạng thái không hợp lệ (400)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches validation exceptions thrown when request body validation fails.
     * Returns an HTTP 400 Bad Request status with a map of field-specific error messages.
     *
     * @param ex the caught MethodArgumentNotValidException
     * @return ResponseEntity containing validation error maps
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Comment: Lặp qua toàn bộ lỗi ràng buộc dữ liệu để lưu vào danh sách lỗi chi tiết
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Comment: Tạo cấu trúc phản hồi lỗi định dạng chuẩn REST
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all fallback generic exceptions.
     * Returns an HTTP 500 Internal Server Error.
     *
     * @param ex the caught Exception
     * @return ResponseEntity containing general error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        // Comment: Ghi log và trả về thông báo chung khi có lỗi hệ thống không lường trước
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected system error occurred: " + ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
