package com.VietBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "User_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Gioi_Tinh")
    private boolean gioiTinh;

    @NotBlank(message = "Hãy điền email")
    @Column(name = "Email", nullable = false, length = 255)
    private String email;

    @Column(name = "Dien_Thoai", length = 15)
    private String dienThoai;

    @NotBlank(message = "Hãy điền mật khẩu")
    @Column(name = "Mat_Khau", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String matKhau;

    @NotBlank(message = "Hãy điền tên đăng nhập")
    @Column(name = "Ten_Dang_Nhap", nullable = false, length = 255)
    private String tenDangNhap;

    @NotBlank(message = "Hãy điền tên người dùng")
    @Column(name = "Ten_Nguoi_Dung", nullable = false, length = 255)
    private String tenNguoiDung;

    @Temporal(TemporalType.DATE)
    @Column(name = "Ngay_Tao", nullable = false)
    private LocalDate ngayTao;

    @Column(name = "Vai_Tro", nullable = false, length = 255)
    private String vaiTro;

    @Column(name = "Hinh_Dai_Dien")
    private String hinhDaiDien;

    @Temporal(TemporalType.DATE)
    @Column(name = "Ngay_Sinh", nullable = false)
    private LocalDate ngaySinh;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaiViet> baiViet;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThanhVien> thanhVien;
}
