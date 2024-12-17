package com.VietBlog.service;

import com.VietBlog.constraints.User.Font_User;
import com.VietBlog.constraints.User.Theme_User;
import com.VietBlog.constraints.User.VaiTro_User;
import com.VietBlog.entity.User;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.LuotLike_BaiViet_Repository;
import com.VietBlog.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

	private final Cloudinary cloudinary;
	private final UserRepository userRepository;
	private final LuotLike_BaiViet_Repository luotLike_BaiViet_Repository;
	private final LuotFollowRepository luotFollowRepository;

	@Autowired
	public UserService(Cloudinary cloudinary, UserRepository userRepository, LuotLike_BaiViet_Repository luotLike_BaiViet_Repository, LuotFollowRepository luotFollowRepository) {
		this.cloudinary = cloudinary;
		this.userRepository = userRepository;
		this.luotLike_BaiViet_Repository = luotLike_BaiViet_Repository;
		this.luotFollowRepository = luotFollowRepository;
	}

	// tìm theo email
	public User timTheoEmail(String email) {
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

		User user = optionalUser.orElseThrow(() -> new RuntimeException("Tên đăng nhập không đúng"));

		// Kiểm tra user và mật khẩu
		if (user != null && user.getMatKhau().equals(matKhau)) {
			return user; // Trả về user nếu đăng nhập thành công
		} else {
			throw new RuntimeException("Mật khẩu không đúng"); // Trả về null nếu đăng nhập thất bại
		}
	}


	public void dangKy(User user) {
		user.setNgayTao(LocalDate.now());
		user.setVaiTro(VaiTro_User.USER);
		user.setTheme(Theme_User.LIGHT);
		user.setFont(Font_User.HELVETICA_NEUE);

		if (userRepository.existsByEmail(user.getEmail())) {
			return;
		}
		if (userRepository.existsByDienThoai(user.getDienThoai())) {
			return;
		}
		if (userRepository.existsByTenDangNhap(user.getTenDangNhap())) {
			return;
		}

		userRepository.save(user);
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
		existingUser.setTheme(user.getTheme() != null ? user.getTheme() : existingUser.getTheme());
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

	// Tìm kiếm người dùng theo tên người dùng
	public User findByTenNguoiDung(String tenNguoiDung) {
		return userRepository.findByTenNguoiDung(tenNguoiDung).orElse(null);
	}

	//phương thức xóa nhóm và xoá toàn bộ user có trong nhóm đó
	public void xoaUser(User user) {
		userRepository.delete(user);
	}

	public int countLikesByUserId(Long userId){
		return luotLike_BaiViet_Repository.countLuotLike_BaiVietByUserId(userId);
	}

	public int countFollowsByUserId(Long userId){
		return luotFollowRepository.countFollowersByUserId(userId);
	}

}

