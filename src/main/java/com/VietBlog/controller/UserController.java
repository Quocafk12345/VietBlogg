package com.VietBlog.controller;

import com.VietBlog.entity.*;
import com.VietBlog.handle.FollowStatusWebSocketHandler;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@SessionAttributes("currentUser")
public class UserController {

	private final UserService userService;
	private final LuotFollowService luotFollowService;
	private final LuotFollowRepository luotFollowRepository;
	private final FollowStatusWebSocketHandler webSocketHandler;
	private final BlockUserService blockUserService;
	private final BlockUserRepository blockUserRepository;

	@Autowired
	public UserController(UserService userService, LuotFollowService luotFollowService, LuotFollowRepository luotFollowRepository, BaiVietRepository baiVietRepository, BaiVietService baiVietService, LuotLike_BaiViet_Service luotLike_BaiViet_Service, UserRepository userRepository, FollowStatusWebSocketHandler webSocketHandler, BlockUserService blockUserService, BlockUserRepository blockUserRepository) {
		this.userService = userService;
		this.luotFollowService = luotFollowService;
		this.luotFollowRepository = luotFollowRepository;
        this.webSocketHandler = webSocketHandler;
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
	@GetMapping("/{userId}/isFollowing")
	public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId, @RequestParam Long userFollowId) {
		try {
			boolean isFollowing = luotFollowService.isFollowing(userFollowId, userId);
			return ResponseEntity.ok(isFollowing);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
	@PostMapping("/{userId}/toggleFollow")
	public ResponseEntity<?> toggleFollow(@PathVariable("userId") Long userId, @RequestParam Long userFollowId) {
		try {
			boolean isFollowing = luotFollowService.toggleFollow(userId,userFollowId);
			webSocketHandler.sendFollowStatusChange(userId, !isFollowing);
			return ResponseEntity.ok(isFollowing);
		}catch (RuntimeException e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/{userId}/toggleBlock")
	public ResponseEntity<?> toggleBlock(@PathVariable("userId") Long userId, @RequestParam Long blockUserId){
		try {
			boolean isBlocking = blockUserService.toggleBlock(blockUserId,userId);
			webSocketHandler.sendFollowStatusChange(userId, !isBlocking);
			return ResponseEntity.ok(isBlocking);
		}catch (RuntimeException e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{userId}/checkBlockStatus")
	public ResponseEntity<?> checkBlockStatus(@PathVariable("userId") Long userId, @RequestParam Long blockUserId){
		BlockUser_ID blockUserID = new BlockUser_ID(userId,blockUserId);
		boolean isBlocking = blockUserRepository.existsById(blockUserID);

		Map<String, Boolean> response = new HashMap<>();
		response.put("isBlocking", isBlocking);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{userId}/checkFollowStatus")
	public ResponseEntity<?> checkFollowStatus(@PathVariable("userId") Long userId, @RequestParam Long UserFollowId){
		LuotFollowId luotFollowId = new LuotFollowId(userId,UserFollowId);
		boolean isFollowing = luotFollowRepository.existsById(luotFollowId);

		Map<String, Boolean> response = new HashMap<>();
		response.put("isFollowing", isFollowing);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Đếm số lượt follow", description = "Đếm số lượt follow của một người dung")
	@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = LuotFollow.class)))
	@ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
	@GetMapping("/{id}/luot-follow")
	public ResponseEntity<Integer> demLuotFollow(@PathVariable Long id) {
		Integer luotFollow = luotFollowRepository.countFollowersByUserId(id);
		return ResponseEntity.ok(luotFollow);
	}

	@GetMapping("/follower/{userId}")
	public ResponseEntity<List<User>> getFollower(@PathVariable Long userId) {
		List<User> followere = luotFollowService.layDanhSachFollowers(userId);
		return ResponseEntity.ok(followere);
	}
	// Lấy danh sách người theo dõi
	@GetMapping("/{userId}/followers")
	public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
		try {
			List<User> followers = luotFollowService.layDanhSachFollowers(userId);
			// Chuyển đổi User entity thành DTO để tránh trả về thông tin nhạy cảm
			List<UserResponse> response = followers.stream()
					.map(user -> new UserResponse(user.getId(), user.getTenNguoiDung(), user.getEmail()))
					.collect(Collectors.toList());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Lấy danh sách người đang theo dõi
	@GetMapping("/{userId}/following")
	public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long userId) {
		try {
			List<User> following = luotFollowService.layDanhSachFollowing(userId);
			// Chuyển đổi User entity thành DTO
			List<UserResponse> response = following.stream()
					.map(user -> new UserResponse(user.getId(), user.getTenNguoiDung(), user.getEmail()))
					.collect(Collectors.toList());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
