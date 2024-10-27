package com.VietBlog.controller;

import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@SessionAttributes("user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("identifier") String identifier,
	                               @RequestParam("password") String password,
	                               Model model) {
		try {
			User user = userService.dangNhap(identifier, password);

			model.addAttribute("user", user); // Lưu user vào model trước
			return ResponseEntity.ok(user);   // Sau đó mới trả về response
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return ResponseEntity.ok("Đăng xuất thành công");
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		try {
			userService.updateUser(user);
			return ResponseEntity.ok("Cập nhật thông tin người dùng thành công");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/upload-avatar")
	public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file,
	                                      @RequestParam("userId") Long userId) {
		try {
			User user = userService.findById(userId);
			if (user == null) {
				return ResponseEntity.notFound().build();
			}
			userService.luuHinhDaiDien(file, user);
			return ResponseEntity.ok("Tải lên hình đại diện thành công");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
