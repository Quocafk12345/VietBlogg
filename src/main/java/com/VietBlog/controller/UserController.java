package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.UserRepository;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.LuotFollowService;
import com.VietBlog.service.LuotLike_BaiViet_Service;
import com.VietBlog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@SessionAttributes("currentUser")
public class UserController {

	private final UserService userService;
	private final LuotFollowService luotFollowService;
	private final LuotFollowRepository luotFollowRepository;
	private final BaiVietRepository baiVietRepository;
	private final BaiVietService baiVietService;
	private final LuotLike_BaiViet_Service luotLike_BaiViet_Service;
	private final UserRepository userRepository;

	@Autowired
	public UserController(UserService userService, LuotFollowService luotFollowService, LuotFollowRepository luotFollowRepository, BaiVietRepository baiVietRepository, BaiVietService baiVietService, LuotLike_BaiViet_Service luotLike_BaiViet_Service, UserRepository userRepository) {
		this.userService = userService;
		this.luotFollowService = luotFollowService;
		this.luotFollowRepository = luotFollowRepository;
		this.baiVietRepository = baiVietRepository;
		this.baiVietService = baiVietService;
		this.luotLike_BaiViet_Service = luotLike_BaiViet_Service;
		this.userRepository = userRepository;
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

	@PostMapping("/{userFollowId}/toggleFollow")
	public ResponseEntity<?> toggleFollow(@PathVariable("userFollowId") Long userFollowId, @RequestParam Long userId) {
		if (userFollowId.equals(userId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn không thể tự follow chính mình.");
		}
		try {
			boolean isFollowed = luotFollowService.toggleFollow(userFollowId, userId);
			String message = isFollowed ? "Follow thành công." : "Unfollow thành công.";
			return ResponseEntity.ok(message);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}



	@Operation(summary = "Đếm số lượt follow", description = "Đếm số lượt follow của một người dung")
	@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = LuotFollow.class)))
	@ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
	@GetMapping("/{id}/luot-follow")
	public ResponseEntity<Integer> demLuotFollow(@PathVariable Long id) {
		Integer luotFollow = luotFollowRepository.countFollowersByUserId(id);
		return ResponseEntity.ok(luotFollow);
	}
}