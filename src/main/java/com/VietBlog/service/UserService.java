package com.VietBlog.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.VietBlog.constraints.User.CoChu_User;
import com.VietBlog.constraints.User.FontChu_User;
import com.VietBlog.constraints.User.MauNen_User;
import com.VietBlog.constraints.User.VaiTro_User;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VietBlog.entity.User;
import com.VietBlog.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

	private final Cloudinary cloudinary;

    private final UserRepository userRepository;

    @Autowired
    public UserService(Cloudinary cloudinary, UserRepository userRepository) {
	    this.cloudinary = cloudinary;
	    this.userRepository = userRepository;
    }

	// tìm theo email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

	public User dangNhap(String identifier, String matKhau) {
		Optional<User> optionalUser;
		if (identifier.contains("@")) {
			optionalUser = userRepository.findByEmail(identifier);
		} else if (identifier.matches("\\d+")) {
			optionalUser = userRepository.findByDienThoai(identifier);
		} else {
			optionalUser = userRepository.findByTenDangNhap(identifier);
		}

		User user = optionalUser.orElse(null);

		// Kiểm tra user và mật khẩu
		if (user != null && user.getMatKhau().equals(matKhau)) {
			return user; // Trả về user nếu đăng nhập thành công
		} else {
			return null; // Trả về null nếu đăng nhập thất bại
		}
	}


	public void dangKy(User user) {
		user.setNgayTao(LocalDate.now());
		user.setVaiTro(VaiTro_User.USER);
		user.setCoChu(CoChu_User.NHO);
		user.setFontChu(FontChu_User.HELVETICA_NEUE);
		user.setMauNen(MauNen_User.WHITE);


		if (isEmailExists(user.getEmail())) {
			return;
		}
		if (isDienThoaiExists(user.getDienThoai())) {
			return;
		}
		if (isTenDangNhapExists(user.getTenDangNhap())) {
			return;
		}

		userRepository.save(user);
	}

	// kiểm tra email đã tồn tại hay chưa
	public boolean isEmailExists(String email) {
		return userRepository.existsByEmail(email);
	}

	// tìm theo SDT
	public User findByDienThoai(String dienThoai) {
        return userRepository.findByDienThoai(dienThoai).orElse(null);
    }

	// cập nhật User
	@Transactional
	public void updateUser(User user) {
		if (user.getId() == null) {
			return;
		}

		User existingUser = userRepository.findById(user.getId()).orElse(null);

		// Kiểm tra email có rỗng, đã tồn tại hay chưa
		if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
			return; // Không cập nhật nếu email đã tồn tại
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

	// lưu hình đại diện
	@SuppressWarnings("unchecked")
	public void luuHinhDaiDien(MultipartFile file, User user) throws IOException {
		Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		user.setHinhDaiDien(uploadResult.get("secure_url").toString());
		userRepository.save(user);
	}

	// tìm User theo id
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
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

	public void xoaUser(User user) {
		userRepository.delete(user);
	}
}

