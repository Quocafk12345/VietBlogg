package com.VietBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.VietBlog.entity.User;
import com.VietBlog.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user) {
        user.setNgayTao(LocalDate.now());
        return userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void updateUser(User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.findById(Long.valueOf(user.getId())).orElse(null);
            if (existingUser != null) {
                if (user.getTenNguoiDung() != null && !user.getTenNguoiDung().isEmpty()) {
                    existingUser.setTenNguoiDung(user.getTenNguoiDung());
                }
                if (user.getTenDangNhap() != null && !user.getTenDangNhap().isEmpty()) {
                    existingUser.setTenDangNhap(user.getTenDangNhap());
                }
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    existingUser.setEmail(user.getEmail());
                }
                // ... (cập nhật các thuộc tính khác)
                // Cập nhật mật khẩu
                if (user.getMatKhau() != null && !user.getMatKhau().isEmpty()) {
                    existingUser.setMatKhau(user.getMatKhau());
                }
            }
        }
    }

    public void luuHinhAnh(MultipartFile file, User user) {
        String tenHinhAnh = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        user.setHinhDaiDien(tenHinhAnh);

        userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}