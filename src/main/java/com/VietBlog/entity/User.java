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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.sql.Date ngayTao;
    private java.sql.Date ngaySinh;
    private String vaiTro;
    @Column(name = "Gioi_Tinh")
    private Boolean gioiTinh;
    private String hinhDaiDien;
    @Column(name = "Mau_Nen")
    private String mauNen;
    @Column(name = "Font_Chu")
    private String fontChu;
    @Column(name = "Co_Chu")
    private String coChu;
}
