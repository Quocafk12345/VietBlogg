package com.VietBlog.entity;
public class UserResponse {
    private Long id;
    private String tenNguoiDung;
    private String email;

    public UserResponse(Long id, String tenNguoiDung, String email) {
        this.id = id;
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
    }

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

