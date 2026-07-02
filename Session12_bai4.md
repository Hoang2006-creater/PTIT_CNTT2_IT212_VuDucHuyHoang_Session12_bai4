# BÁO CÁO THIẾT KẾ KIỂM THỬ & KIỂM THỬ TỰ ĐỘNG (TEST CASES & AUTOMATION SUITE)
## PHÂN HỆ: ĐĂNG KÝ MỞ TÀI KHOẢN NGÂN HÀNG (eKYC ONBOARDING)
### THƯ MỤC NỘP BÀI: `Session12_bai4`

---

## 1. YÊU CẦU 1: BẢNG PHÂN TÍCH TEST CASES CHI TIẾT (IEEE 829 STANDARD)

Dưới đây là danh sách toàn bộ các Test Case (Positive, Negative, Boundary, Security) được thiết kế cho API `POST /api/accounts/register`:

| Test ID | Phân nhóm | Test Scenario (Kịch bản kiểm thử) | Dữ liệu đầu vào (Input Payload) | Expected Result (Kết quả kỳ vọng) | Priority (Độ ưu tiên) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-POS-01** | Positive | Đăng ký thành công với dữ liệu hợp lệ | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `201 Created`. Trả về `accountId`, `accountNumber` (10 số), `status = PENDING`. | High |
| **TC-NEG-01** | Negative | `fullName` bị rỗng | `{"fullName": "", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request`. Thông báo: *"fullName must not be blank"*. | High |
| **TC-NEG-02** | Negative | `phone` bị rỗng | `{"fullName": "Nguyen Van A", "phone": "", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request`. Thông báo: *"phone must not be blank"*. | High |
| **TC-NEG-03** | Negative | `email` sai định dạng (Thiếu `@` và tên miền) | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "vanagmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request`. Thông báo: *"email must be in valid email format"*. | High |
| **TC-NEG-04** | Negative | `citizenId` bị trùng lặp trong hệ thống | `{"fullName": "Nguyen Van B", "phone": "0911223344", "email": "vanb@gmail.com", "citizenId": "012345678901"}` | HTTP Status `409 Conflict`. Thông báo: *"already exists in the system"*. | High |
| **TC-NEG-05** | Negative | `citizenId` chứa ký tự chữ cái (Sai định dạng) | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678abc"}` | HTTP Status `400 Bad Request`. Thông báo: *"citizenId must be exactly 12 numeric digits"*. | High |
| **TC-NEG-06** | Negative | Dữ liệu đầu vào bị `null` hoàn toàn | `{}` | HTTP Status `400 Bad Request` hoặc báo lỗi Json validation. | High |
| **TC-NEG-07** | Negative | Trường dữ liệu chỉ chứa khoảng trắng | `{"fullName": "   ", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request`. Báo lỗi validation. | Medium |
| **TC-NEG-08** | Negative | `fullName` chứa các ký tự đặc biệt nguy hiểm | `{"fullName": "Nguyễn @#$%^", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | Hệ thống từ chối hoặc lọc ký tự đặc biệt tùy theo thiết kế nghiệp vụ ngân hàng. | Medium |
| **TC-SEC-01** | Security | Tấn công SQL Injection qua trường `fullName` | `{"fullName": "Nguyen Van A' OR 1=1 --", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `201 Created` bình thường (do sử dụng Hibernate PreparedStatement tự động bind tham số), không xảy ra SQL Injection. | High |
| **TC-SEC-02** | Security | Tấn công XSS qua trường `fullName` | `{"fullName": "<script>alert('XSS')</script>", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | Hệ thống phải encode HTML entity hoặc sanitize dữ liệu đầu vào trước khi phản hồi để tránh XSS. | High |
| **TC-BOU-01** | Boundary | `fullName` có độ dài tối thiểu (2 ký tự) | `{"fullName": "Le", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `201 Created` thành công. | Medium |
| **TC-BOU-02** | Boundary | `fullName` quá dài (ví dụ: 1000 ký tự) | `{"fullName": "Nguyen Van A A A ... [1000 ký tự]", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request` do quá giới hạn độ dài trường trong DB. | Medium |
| **TC-BOU-03** | Boundary | `phone` ngắn hơn quy định Việt Nam (8 số) | `{"fullName": "Nguyen Van A", "phone": "12345678", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request` (Nếu có rule định dạng SĐT). | High |
| **TC-BOU-04** | Boundary | `phone` dài hơn quy định Việt Nam (12 số) | `{"fullName": "Nguyen Van A", "phone": "0987654321123", "email": "vana@gmail.com", "citizenId": "012345678901"}` | HTTP Status `400 Bad Request`. | High |
| **TC-BOU-05** | Boundary | `citizenId` có 11 chữ số (Thiếu 1 số) | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "01234567890"}` | HTTP Status `400 Bad Request`. Thông báo: *"citizenId must be exactly 12 numeric digits"*. | High |
| **TC-BOU-06** | Boundary | `citizenId` có 13 chữ số (Thừa 1 số) | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "vana@gmail.com", "citizenId": "0123456789012"}` | HTTP Status `400 Bad Request`. Thông báo: *"citizenId must be exactly 12 numeric digits"*. | High |
| **TC-BOU-07** | Boundary | `email` có độ dài tối đa cho phép | `{"fullName": "Nguyen Van A", "phone": "0987654321", "email": "a[250 ký tự]@gmail.com", "citizenId": "012345678901"}` | Hệ thống xử lý thành công hoặc trả lỗi 400 Bad Request tùy theo cấu hình max email size. | Low |

---

## 2. YÊU CẦU 2: DỮ LIỆU KIỂM THỬ THỰC TẾ (TEST DATA - 50 DÒNG CSV)

Tệp dữ liệu kiểm thử thực tế đã được sinh thành công và lưu trữ tại đường dẫn:  
📂 **[test_data.csv](file:///c:/Users/Admin/OneDrive/Desktop/IT212/Session12_bai4/test_data.csv)**

### Cấu trúc dữ liệu mẫu (10 dòng đầu tiên):
```csv
fullName,phone,email,citizenId
Nguyen Van An,0901234567,an.nguyen@gmail.com,001095000001
Tran Thi Binh,0912345678,binh.tran@yahoo.com,001095000002
Le Hoang Cuong,0983456789,cuong.le@outlook.com,001095000003
Pham Minh Duc,0864567890,duc.pham@gmail.com,001095000004
Hoang Thanh Em,0355678901,em.hoang@gmail.com,001095000005
Vu Thi Phuong,0766789012,phuong.vu@yahoo.com,001095000006
Ngo Van Giang,0587890123,giang.ngo@gmail.com,001095000007
Do Minh Hai,0908901234,hai.do@outlook.com,001095000008
Bui Thi Hoa,0919012345,hoa.bui@gmail.com,001095000009
Dang Van Hung,0989123456,hung.dang@yahoo.com,001095000010
```

---

## 3. YÊU CẦU 3 & 4: MÃ NGUỒN UNIT TEST (JUNIT 5 & MOCKITO)

Các tệp Unit Test đã được triển khai hoàn chỉnh với đầy đủ Javadoc bằng tiếng Anh và chú thích (comment) từng bước kiểm thử bằng tiếng Việt.

### 3.1. Unit Test cho Tầng Service:
*   Đường dẫn: [AccountServiceTest.java](file:///c:/Users/Admin/OneDrive/Desktop/IT212/Session12_bai4/src/test/java/com/bank/ekyc/service/AccountServiceTest.java)
*   **Các trường hợp kiểm thử:**
    *   `testRegisterAccount_Success()`: Kiểm thử đăng ký thành công khi CSDL hợp lệ.
    *   `testRegisterAccount_DuplicateCitizenId()`: Kiểm thử ném lỗi `DuplicateCitizenIdException` khi số CCCD đã tồn tại.
    *   `testRegisterAccount_RepositoryError()`: Kiểm thử lỗi kết nối cơ sở dữ liệu (Database rollback).

### 3.2. Unit Test cho Tầng Controller:
*   Đường dẫn: [AccountControllerTest.java](file:///c:/Users/Admin/OneDrive/Desktop/IT212/Session12_bai4/src/test/java/com/bank/ekyc/controller/AccountControllerTest.java)
*   **Các trường hợp kiểm thử:**
    *   `testRegisterAccount_Success()`: Kiểm tra HTTP status 201 Created và định dạng JSON phản hồi đúng.
    *   `testRegisterAccount_DuplicateCitizenId()`: Kiểm tra HTTP status 409 Conflict.
    *   `testRegisterAccount_InvalidEmailFormat()`: Kiểm tra validation chặn định dạng Email và trả về mã lỗi 400 Bad Request.
    *   `testRegisterAccount_InvalidCitizenIdFormat()`: Kiểm tra validation chặn định dạng Số CCCD không đúng 12 chữ số và trả về mã 400 Bad Request.

---

## 4. YÊU CẦU 5: HƯỚNG DẪN CHẠY UNIT TEST BẰNG MAVEN

Để thực hiện chạy kiểm thử tự động, bạn có thể thực hiện theo các bước sau:

1.  Mở giao diện dòng lệnh (Command Prompt hoặc PowerShell).
2.  Di chuyển con trỏ vào thư mục của Bài 04:
    ```bash
    cd c:\Users\Admin\OneDrive\Desktop\IT212\Session12_bai4
    ```
3.  Chạy toàn bộ Unit Test và xuất kết quả biên dịch:
    ```bash
    mvn clean test
    ```
    Hệ thống sẽ biên dịch dự án, tải các thư viện cần thiết, thực hiện chạy độc lập các ca kiểm thử bằng Mockito, và in ra bảng tổng hợp kết quả (Pass/Fail).