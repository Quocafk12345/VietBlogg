package com.VietBlog.service;

import java.io.IOException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VietBlog.entity.User;
import com.VietBlog.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user) {
        user.setNgayTao(LocalDate.now()); // Gán ngày tạo là ngày hiện tại
        return userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public User findByDienThoai(String dienThoai) {
        return userRepository.findByDienThoai(dienThoai).orElse(null);
    }

	@Transactional
	public void updateUser(User user) {
		if (user.getId() != null) {
			User existingUser = userRepository.findById(user.getId()).orElse(null);
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
				if (user.getMauNen() != null) {
					existingUser.setMauNen(user.getMauNen());
				}
				if (user.getFontChu() != null) {
					existingUser.setFontChu(user.getFontChu());
				}
				if (user.getCoChu() != null) {
					existingUser.setCoChu(user.getCoChu());
				}
			}
		}
	}
    public void luuHinhAnh(MultipartFile file, User user) throws IOException {
        byte[] hinhAnhBytes = file.getBytes();
        String base64Image = java.util.Base64.getEncoder().encodeToString(hinhAnhBytes);
        user.setHinhDaiDien(base64Image);
        userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
}

