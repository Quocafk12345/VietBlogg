package com.VietBlog.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.VietBlog.constraints.User.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "Users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "User_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Gioi_Tinh")
    private boolean gioiTinh;

    @NotBlank(message = "Hãy điền email")
    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Dien_Thoai", length = 15)
    private String dienThoai;

    @NotBlank(message = "Hãy điền mật khẩu")
    @Column(name = "Mat_Khau", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String matKhau;

    @NotBlank(message = "Hãy điền tên đăng nhập")
    @Column(name = "Ten_Dang_Nhap", nullable = false)
    private String tenDangNhap;

    @NotBlank(message = "Hãy điền tên người dùng")
    @Column(name = "Ten_Nguoi_Dung", nullable = false)
    private String tenNguoiDung;

    @Temporal(TemporalType.DATE)
    @Column(name = "Ngay_Tao", nullable = false)
    private LocalDate ngayTao;

    @Convert(converter = VaiTro_User_Converter.class)
    @Column(name = "Vai_Tro", nullable = false)
    private VaiTro_User vaiTro;

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

    @Convert(converter = MauNen_Converter.class)
    @Column(name = "Mau_Nen")
    private MauNen_User mauNen;

    @Convert(converter = FontChu_Converter.class)
    @Column(name = "Font_Chu")
    private FontChu_User fontChu;

    @Convert(converter = CoChu_Converter.class)
    @Column(name = "Co_Chu")
    private CoChu_User coChu;
}
