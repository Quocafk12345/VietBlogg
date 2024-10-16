package com.VietBlog.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String tenDangNhap;
    private String tenNguoiDung;
    private String email;
    private String matKhau;
    private String dienThoai;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Định dạng ngày theo chuẩn từ form
    private Date ngayTao;
    private String vaiTro;
    private Boolean gioiTinh;

    // Constructors, getters, and setters
}
