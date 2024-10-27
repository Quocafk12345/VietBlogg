package com.VietBlog.service;

import java.io.IOException;
import java.time.LocalDate;

import com.VietBlog.constraints.User.CoChu_User;
import com.VietBlog.constraints.User.FontChu_User;
import com.VietBlog.constraints.User.MauNen_User;
import com.VietBlog.constraints.User.VaiTro_User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VietBlog.entity.User;
import com.VietBlog.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

	private HttpSession session;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, HttpSession session) {
        this.userRepository = userRepository;
		this.session = session;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

	public User dangKy(User user) {
		user.setNgayTao(LocalDate.now()); // Gán ngày tạo là ngày hiện tại
		user.setVaiTro(VaiTro_User.USER);
		user.setCoChu(CoChu_User.NHO);
		user.setFontChu(FontChu_User.HELVETICA_NEUE);
		user.setMauNen(MauNen_User.WHITE);
		return userRepository.save(user);
	}

	public boolean isEmailExists(String email) {
		return userRepository.existsByEmail(email);
	}

	public User findByDienThoai(String dienThoai) {
        return userRepository.findByDienThoai(dienThoai).orElse(null);
    }

	@Transactional
	public void updateUser(User user) {
		if (user.getId() == null) {
			throw new IllegalArgumentException("ID người dùng không được null");
		}

		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

		// Kiểm tra email có rỗng, đã tồn tại hay chưa
		if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email đã tồn tại");
		}

		// Cập nhật các thuộc tính
		existingUser.setTenNguoiDung(user.getTenNguoiDung() != null ? user.getTenNguoiDung() : existingUser.getTenNguoiDung());
		existingUser.setTenDangNhap(user.getTenDangNhap() != null ? user.getTenDangNhap() : existingUser.getTenDangNhap());
		existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
		existingUser.setMatKhau(user.getMatKhau() != null ? user.getMatKhau() : existingUser.getMatKhau());
		existingUser.setMauNen(user.getMauNen() != null ? user.getMauNen() : existingUser.getMauNen());
		existingUser.setFontChu(user.getFontChu() != null ? user.getFontChu() : existingUser.getFontChu());
		existingUser.setCoChu(user.getCoChu() != null ? user.getCoChu() : existingUser.getCoChu());

		userRepository.save(existingUser);
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

	// Đăng xuất
	public void logout(HttpSession session) {
		session.removeAttribute("user");
	}

	// Kiểm tra xem số điện thoại đã tồn tại chưa
	public boolean isDienThoaiExists(String dienThoai) {
		return userRepository.existsByDienThoai(dienThoai);
	}

	// Kiểm tra xem tên đăng nhập đã tồn tại chưa
	public boolean isTenDangNhapExists(String tenDangNhap) {
		return userRepository.existsByTenDangNhap(tenDangNhap);
	}

	// Tìm kiếm người dùng theo tên người dùng
	public User findByTenNguoiDung(String tenNguoiDung) {
		return userRepository.findByTenNguoiDung(tenNguoiDung).orElse(null);
	}
}

