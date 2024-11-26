package com.VietBlog.repository;

import com.VietBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTenDangNhap(String tenDangNhap);

    Optional<User> findByDienThoai(String dienThoai);

    // Tìm kiếm người dùng theo tên người dùng
    Optional<User> findByTenNguoiDung(String tenNguoiDung);

    // Kiểm tra xem email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Kiểm tra xem số điện thoại đã tồn tại chưa
    boolean existsByDienThoai(String dienThoai);

    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    boolean existsByTenDangNhap(String tenDangNhap);

}
