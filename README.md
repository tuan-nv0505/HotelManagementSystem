# Hotel System Spring Project

Dự án quản lý khách sạn sử dụng Spring Framework, Hibernate và ReactJS.

[//]: # (## Hướng dẫn cài đặt Database)

[//]: # ()
[//]: # (Để đảm bảo bảo mật, thông tin cấu hình database được quản lý qua biến môi trường &#40;Environment Variables&#41;. Cần thiết lập các biến sau trước khi chạy ứng dụng.)

[//]: # ()
[//]: # (### 1. Các biến môi trường cần thiết)

[//]: # (Cần tạo các biến môi trường sau trên máy hoặc trong cấu hình Run của IDE:)

[//]: # ()
[//]: # (| Tên biến | Mô tả | Ví dụ                                |)

[//]: # (| :--- | :--- |:-------------------------------------|)

[//]: # (| `url_db` | Đường dẫn JDBC tới MySQL | `jdbc:mysql://localhost:3306/vidudb` |)

[//]: # (| `username` | Tên đăng nhập database | `root`                               |)

[//]: # (| `password` | Mật khẩu database | `root@123`                           |)

[//]: # ()
[//]: # (### 2. Cách cấu hình trên IntelliJ IDEA)

[//]: # (1. Mở project và chọn **Edit Configurations**.)

[//]: # (2. Chuyển sang tab **Startup/Connection**.)

[//]: # (3. Chọn mục **Run** và tìm phần **Environment variables**.)

[//]: # (4. Thêm các cặp Key/Value tương ứng như bảng trên.)

[//]: # ()
[//]: # (### 3. Cấu hình File &#40;Chỉ dành cho local&#41;)

[//]: # (Đảm bảo file `src/main/resources/databases.properties` của bạn sử dụng placeholder:)

[//]: # (```properties)

[//]: # (hibernate.connection.url=${url_db})

[//]: # (hibernate.connection.username=${username})

[//]: # (hibernate.connection.password=${password})