# Hệ thống Quản lý Khách sạn (Hotel Management System)

Hệ thống quản lý khách sạn được xây dựng nhằm số hóa toàn bộ quy trình vận hành, từ quản lý phòng, đặt phòng, thanh toán đến hỗ trợ khách hàng. Dự án được thiết kế theo kiến trúc đa tầng, tách 
biệt Frontend, Backend và AI service để dễ mở rộng và bảo trì.

Hệ thống backend áp dụng mô hình **Front Controller Pattern** thông qua Spring MVC, đồng thời xây dựng theo kiến trúc **RESTful API** để hỗ trợ giao tiếp giữa frontend và backend một cách thống 
nhất. Ngoài ra, hệ thống còn tích hợp **WebSocket** để xử lý dữ liệu realtime.

---

## Giới thiệu

Hệ thống gồm 3 thành phần chính:

- **Frontend User:** Xây dựng bằng ReactJS, cung cấp giao diện đặt phòng và trải nghiệm người dùng cho khách hàng.
- **Frontend Admin:** Sử dụng Thymeleaf dành cho quản trị viên và nhân viên quản lý hệ thống.
- **Backend Core:** Phát triển bằng Spring MVC kết hợp Hibernate để xử lý nghiệp vụ, bảo mật và làm việc với cơ sở dữ liệu.
- **AI Service:** FastAPI, LangChain và mô hình RAG (Retrieval-Augmented Generation) để triển khai chatbot hỗ trợ khách hàng.

---

## Kiến trúc hệ thống

Hệ thống được chia thành các service độc lập:

1. **Client Interface**
   - ReactJS xử lý giao diện và tương tác người dùng.
   - Giao diện quản trị sử dụng Thymeleaf.

2. **Backend Core**
   - Áp dụng kiến trúc **Front Controller Pattern** với Spring MVC.
   - Mọi request được tiếp nhận và xử lý tập trung thông qua `DispatcherServlet`.
   - Hệ thống API được xây dựng theo chuẩn **RESTful API**.
   - Xử lý nghiệp vụ theo mô hình Controller → Service → Repository.
   - Bảo mật với Spring Security + JWT.
   - Hibernate dùng để thao tác dữ liệu với MySQL.
   - Tích hợp **WebSocket** để xử lý các chức năng realtime.

3. **AI Chatbot Service**
   - Xây dựng bằng FastAPI.
   - Tích hợp LangChain và mô hình **RAG (Retrieval-Augmented Generation)**.
   - Chatbot có khả năng truy xuất dữ liệu liên quan trước khi sinh phản hồi nhằm tăng độ chính xác khi hỗ trợ khách hàng.

---

## Tính năng chính

### Quản trị viên & Nhân viên

- Quản lý trạng thái phòng theo thời gian thực.
- Theo dõi lịch bảo trì phòng.
- Quản lý số lượng phòng trống, phòng đã đặt và phòng bảo trì.
- Quản lý quy trình đặt phòng.
- Quản lý dịch vụ đi kèm.
- Theo dõi và quản lý thanh toán, hoàn tiền.
- Đồng bộ dữ liệu realtime thông qua WebSocket.

### Khách hàng

- Tìm kiếm phòng theo ngày nhận/trả phòng.
- Lọc phòng theo sức chứa và nhu cầu sử dụng.
- Đặt phòng và thanh toán trực tuyến qua VNPay.
- Đăng nhập bằng:
  - Tài khoản hệ thống
  - Google OAuth2
  - Facebook OAuth2
- Chat trực tiếp với AI Chatbot hỗ trợ thông tin phòng và dịch vụ khách sạn.
- Nhận cập nhật trạng thái dữ liệu realtime.

---

## Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Frontend User | ReactJS |
| Frontend Admin | Thymeleaf |
| Backend | Java, Spring MVC, Spring Security, RESTful API, WebSocket |
| Design Pattern | Front Controller Pattern |
| ORM & Database | Hibernate, MySQL |
| Authentication | JWT, OAuth2 |
| AI Service | Python, FastAPI, LangChain, RAG |
| Build Tools | Maven, npm, yarn |
