package com.VietBlog.controller;

import com.VietBlog.entity.BlockUser_ID;
import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.BlockUserRepository;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.UserRepository;
import com.VietBlog.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final LuotFollowService luotFollowService;
	private final LuotFollowRepository luotFollowRepository;
	private final BlockUserService blockUserService;
	private final BlockUserRepository blockUserRepository;

	@Autowired
	public UserController(UserService userService, LuotFollowService luotFollowService, LuotFollowRepository luotFollowRepository, BaiVietRepository baiVietRepository, BaiVietService baiVietService, LuotLike_BaiViet_Service luotLike_BaiViet_Service, UserRepository userRepository, BlockUserService blockUserService, BlockUserRepository blockUserRepository) {
		this.userService = userService;
		this.luotFollowService = luotFollowService;
		this.luotFollowRepository = luotFollowRepository;
		this.blockUserService = blockUserService;
		this.blockUserRepository = blockUserRepository;
	}

	@Operation(summary = "Đăng nhập tài khoản", description = "Nhận thông tin chi tiết của người dùng và lưu vào session.")
	@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = User.class)))
	@ApiResponse(responseCode = "404", description = "Không tìm thấy tài khoản", content = @Content(schema = @Schema(implementation = User.class)))
	@PostMapping("/dang-nhap")
	public ResponseEntity<?> login(@RequestParam("identifiers") String identifier,
	                               @RequestParam("password") String password) {
		try {
			return ResponseEntity.ok(userService.dangNhap(identifier, password)); // Sau đó mới trả về response
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
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

	@PostMapping("/{userId}/theo-doi")
	public ResponseEntity<?> toggleFollow(@PathVariable("userId") Long userId, @RequestParam Long userFollowId) {
		try {
			return ResponseEntity.ok(luotFollowService.toggleFollow(userId, userFollowId));
		}catch (RuntimeException e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/{userId}/chan")
	public ResponseEntity<?> toggleBlock(@PathVariable("userId") Long userId, @RequestParam Long blockUserId){
		try {
			return ResponseEntity.ok(blockUserService.toggleBlock(blockUserId, userId));
		}catch (RuntimeException e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{userId}/chan/kiem-tra")
	public ResponseEntity<Boolean> checkBlockStatus(@PathVariable("userId") Long userId, @RequestParam Long blockUserId) {
		BlockUser_ID blockUserID = new BlockUser_ID(userId,blockUserId);
		return ResponseEntity.ok(blockUserRepository.existsById(blockUserID));
	}

	@GetMapping("/{userId}/theo-doi/kiem-tra")
	public ResponseEntity<Boolean> checkFollowStatus(@PathVariable Long userId, @RequestParam Long userFollowId) {
		LuotFollowId followId = new LuotFollowId(userFollowId, userId);
		return ResponseEntity.ok(luotFollowRepository.existsById(followId));
	}

	@Operation(summary = "Đếm số lượt follow", description = "Đếm số lượt follow của một người dung")
	@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = LuotFollow.class)))
	@ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
	@GetMapping("/{id}/luot-follow")
	public ResponseEntity<Integer> demLuotFollow(@PathVariable Long id) {
		Integer luotFollow = luotFollowRepository.demSoLuongNguoiDuocTheoDoi(id);
		return ResponseEntity.ok(luotFollow);
	}

	@GetMapping("/following/{userId}")
	public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
		List<User> following = luotFollowService.layDanhSachFollowing(userId);
		return ResponseEntity.ok(following);
	}

	@GetMapping("/follower/{userId}")
	public ResponseEntity<List<User>> getFollower(@PathVariable Long userId) {
		List<User> follower = luotFollowService.layDanhSachFollowers(userId);
		return ResponseEntity.ok(follower);
	}
}