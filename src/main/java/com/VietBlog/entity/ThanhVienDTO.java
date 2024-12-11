package com.VietBlog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhVienDTO {
    private String tenNguoiDung;
    private String vaiTro;
    private String hinhDaiDien;
    private Long userId;
}

// Data Transfer Object (DTO), nó chỉ được sử dụng để chuyển đổi dữ liệu giữa các tầng của ứng dụng
// (trong trường hợp này là giữa backend và frontend)
//Giảm thiểu dữ liệu truyền tải: Chỉ chứa các thuộc tính cần thiết cho frontend, tránh truyền tải dữ liệu không cần thiết.
//Che giấu cấu trúc database: Frontend không cần biết cấu trúc chi tiết của các entity trong database.
//Tăng tính linh hoạt: Cho phép thay đổi cấu trúc database mà không ảnh hưởng đến frontend.