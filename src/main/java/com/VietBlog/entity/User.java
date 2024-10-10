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
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    public String getTenNguoiDung() {
        return tenNguoiDung;
    }
    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMatKhau() {
        return matKhau;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public String getDienThoai() {
        return dienThoai;
    }
    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
    public java.sql.Date getNgayTao() {
        return ngayTao;
    }
    public void setNgayTao(java.sql.Date ngayTao) {
        this.ngayTao = ngayTao;
    }
    public java.sql.Date getNgaySinh() {
        return ngaySinh;
    }
    public void setNgaySinh(java.sql.Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    public String getVaiTro() {
        return vaiTro;
    }
    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
    public Boolean getGioiTinh() {
        return gioiTinh;
    }
    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    public String getHinhDaiDien() {
        return hinhDaiDien;
    }
    public void setHinhDaiDien(String hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }
}
