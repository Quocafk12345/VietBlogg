package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String tenDangNhap;
    private String tenNguoiDung;
    private String email;
    private String matKhau;
    private String dienThoai;
    private String hinhDaiDien;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;
    private String vaiTro;
    private Boolean gioiTinh;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BaiViet> baiViets;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<ThongBao> thongBaos;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<BinhLuan> binhLuans;
}
