package com.VietBlog.controller;

import com.VietBlog.entity.User;
import com.VietBlog.service.EmailService;
import com.VietBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class QuenMatKhauController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	private String resetToken; // Biến lưu trữ mã xác thực
	private String email;

	@PostMapping("/forgot-password")
	public String processForgotPassword(Model model, @RequestParam String email) {
		User user = userService.findByEmail(email);

		if (user != null) {
			// Tạo mã xác thực ngẫu nhiên
			this.email = email;
			resetToken = generateResetToken();

			// Gửi mã xác thực qua email
			emailService.sendResetCode(email, resetToken);
			model.addAttribute("message", "Mã xác thực đã được gửi qua email.");
			return "account/verify-token"; // Chuyển đến trang nhập mã xác thực
		} else {
			model.addAttribute("error", "Email không tồn tại!");
			return "account/forgot-password"; // Quay lại trang nhập email
		}
	}

	@PostMapping("/verify-token")
	public String processVerifyToken(Model model, @RequestParam String token) {
		if (resetToken != null && resetToken.equals(token)) {
			// Nếu mã xác thực đúng, cho phép người dùng nhập mật khẩu mới
			return "account/reset-password"; // Chuyển đến trang nhập mật khẩu mới
		} else {
			model.addAttribute("error", "Mã xác thực không hợp lệ! Vui lòng kiểm tra lại.");
			return "account/verify-token"; // Quay lại trang nhập mã xác thực
		}
	}

	@PostMapping("/reset-password")
	public String processResetPassword(@RequestParam String password,
	                                   @RequestParam String confirmPassword,
	                                   Model model) {
		if (!password.equals(confirmPassword)) {
			model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
			return "account/reset-password"; // Quay lại trang nhập mật khẩu mới
		}

		User user = userService.findByEmail(this.email); // Sử dụng email đã lưu
		if (user != null) {
			user.setMatKhau(password); // Cập nhật mật khẩu mới
			userService.updateUser(user); // Lưu vào cơ sở dữ liệu
			model.addAttribute("message", "Mật khẩu đã được cập nhật thành công!");
			return "account/login"; // Chuyển về trang đăng nhập
		} else {
			model.addAttribute("error", "Người dùng không tồn tại!");
			return "account/reset-password"; // Quay lại trang nhập mật khẩu mới
		}
	}

	private String generateResetToken() {
		// Tạo mã xác thực ngẫu nhiên (có thể sử dụng UUID hoặc bất kỳ phương pháp nào
		// khác)
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000)); // Mã 6 chữ số
	}
}
