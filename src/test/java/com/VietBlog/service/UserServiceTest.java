package com.VietBlog.service;

import com.VietBlog.entity.User;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.LuotLike_BaiViet_Repository;
import com.VietBlog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final LuotLike_BaiViet_Repository luotLike_BaiViet_Repository = Mockito.mock(LuotLike_BaiViet_Repository.class);
    private final LuotFollowRepository luotFollowRepository = Mockito.mock(LuotFollowRepository.class);
    private final UserService userService = new UserService(null, userRepository, luotLike_BaiViet_Repository, luotFollowRepository); // Cloudinary không cần thiết cho test này

    @Test
    void dangKy_duLieuHopLe() {
        User user = new User();
        user.setEmail("nqui20042@gmail.com");
        user.setDienThoai("0827876402");
        user.setTenDangNhap("nqui2004");
        user.setGioiTinh(Boolean.parseBoolean("true"));
        user.setNgaySinh(LocalDate.of(2004, 11, 21));
        user.setTenNguoiDung("Nguyễn Anh Qui");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByDienThoai(user.getDienThoai())).thenReturn(false);
        when(userRepository.existsByTenDangNhap(user.getTenDangNhap())).thenReturn(false);

        userService.dangKy(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void dangKy_emailDaTonTai() {
        User user = new User();
        user.setEmail("nqui20042@gmail.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        userService.dangKy(user);

        verify(userRepository, never()).save(user);
    }

    @Test
    void dangKy_dienThoaiDaTonTai() {
        User user = new User();
        user.setDienThoai("0827876402");

        when(userRepository.existsByDienThoai(user.getDienThoai())).thenReturn(true);

        userService.dangKy(user);

        verify(userRepository, never()).save(user);
    }

    @Test
    void dangKy_tenDangNhapDaTonTai() {
        User user = new User();
        user.setTenDangNhap("nqui2004");

        when(userRepository.existsByTenDangNhap(user.getTenDangNhap())).thenReturn(true);

        userService.dangKy(user);

        verify(userRepository, never()).save(user);
    }

    //Test Đăng nhập
    @Test
    void dangNhap_thongTinDangNhapHopLe() {
        User user = new User();
        user.setEmail("nqui20042@gmail.com");
        user.setMatKhau("password123");

        when(userRepository.findByEmail("nqui20042@gmail.com")).thenReturn(Optional.of(user));

        User result = userService.dangNhap("nqui20042@gmail.com", "password123");

        assertEquals(user, result);
    }

    @Test
    void dangNhap_thongTinDangNhapKhongHopLe() {
        when(userRepository.findByEmail("nqui20042@gmail.com")).thenReturn(Optional.empty());

        User result = userService.dangNhap("nqui20042@gmail.com", "password");

        assertNull(result);
    }

    @Test
    void dangNhap_khongTimThayNguoiDung() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User result = userService.dangNhap("nonexistent@example.com", "password123");

        assertNull(result);
    }

    @Test
    void isValidEmail_emailHopLe() {
        assertTrue(userService.isValidEmail("nqui20042@gmail.com"));
        assertTrue(userService.isValidEmail("user.name@subdomain.example.com"));
        assertTrue(userService.isValidEmail("user+alias@example.com"));
    }

    @Test
    void isValidEmail_emailKhongHopLe() {
        assertFalse(userService.isValidEmail("test.example.com"));
        assertFalse(userService.isValidEmail("test@example"));
        assertFalse(userService.isValidEmail("@example.com"));
        assertFalse(userService.isValidEmail("test@.com"));
        assertFalse(userService.isValidEmail("test@com"));
    }

    @Test
    void isValidPhone_soDienThoaiHopLe() {
        assertTrue(userService.isValidPhone("0123456789"));
        assertTrue(userService.isValidPhone("0987654321"));
        assertTrue(userService.isValidPhone("0321456789"));
    }

    @Test
    void isValidPhone_soDienThoaiKhongHopLe() {
        assertFalse(userService.isValidPhone("123456789"));  // Thiếu số 0 ở đầu
        assertFalse(userService.isValidPhone("012345678"));   // Quá ngắn
//        assertFalse(userService.isValidPhone("01234567890")); // Quá dài
        assertFalse(userService.isValidPhone("0123abc6789")); // Chứa ký tự không phải số
        assertFalse(userService.isValidPhone("012 345 6789")); // Chứa khoảng trắng
    }
}