package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@SessionAttributes("currentUser")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Đăng nhập tài khoản", description = "Nhận thông tin chi tiết của người dùng và lưu vào session.")
	@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = BaiViet.class)))
	@ApiResponse(responseCode = "404", description = "Không tìm thấy tài khoản", content = @Content(schema = @Schema(implementation = BaiViet.class)))
	@PostMapping("/dang-nhap")
	public User login(@RequestParam("identifiers") String identifier,
	                               @RequestParam("password") String password) {
		try {
			return userService.dangNhap(identifier, password); // Sau đó mới trả về response
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/dang-xuat")
	public ResponseEntity<?> logout(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return ResponseEntity.ok().body(Map.of("message", "Đăng xuất thành công"));
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
