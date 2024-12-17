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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
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

	private final UserRepository userRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;
	private final LuotFollowService luotFollowService;
	private final LuotFollowRepository luotFollowRepository;
	private final FollowStatusWebSocketHandler webSocketHandler;
	private final BlockUserService blockUserService;
	private final BlockUserRepository blockUserRepository;

	@Autowired
	public UserController(UserService userService, LuotFollowService luotFollowService, LuotFollowRepository luotFollowRepository, BaiVietRepository baiVietRepository, BaiVietService baiVietService, LuotLike_BaiViet_Service luotLike_BaiViet_Service, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, FollowStatusWebSocketHandler webSocketHandler, BlockUserService blockUserService, BlockUserRepository blockUserRepository) {
		this.userService = userService;
		this.luotFollowService = luotFollowService;
		this.luotFollowRepository = luotFollowRepository;
		this.messagingTemplate = messagingTemplate;
		this.webSocketHandler = webSocketHandler;
		this.blockUserService = blockUserService;
		this.blockUserRepository = blockUserRepository;
		this.userRepository = userRepository;
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
	@GetMapping("/{userId}/isFollowing")
	public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId, @RequestParam Long userFollowId) {
		try {
			boolean isFollowing = luotFollowService.kiemTraFollow(userId, userFollowId);
			return ResponseEntity.ok(isFollowing);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
	public void notifyFollowChange(Long userId, Long userFollowId, boolean isFollowing) {
		Map<String, Object> message = new HashMap<>();
		message.put("userId", userId);
		message.put("userFollowId", userFollowId);
		message.put("isFollowing", isFollowing);
		messagingTemplate.convertAndSend("/topic/follow-status", message);
	}


	// Follow user (thêm vào danh sách follow)
	public boolean follow(Long userId, Long userFollowId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User không tồn tại."));
		User currentUser = userRepository.findById(userFollowId)
				.orElseThrow(() -> new RuntimeException("Chưa login."));

		// Tạo ID cho bảng trung gian
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);

		if (luotFollowRepository.existsById(luotFollowId)) {
			return false; // Nếu đã follow rồi thì không làm gì, trả về false
		} else {
			// Nếu chưa follow, thêm bản ghi mới vào bảng trung gian
			LuotFollow luotFollow = new LuotFollow();
			luotFollow.setId(luotFollowId);
			luotFollow.setUser(user);
			luotFollow.setUserFollow(currentUser);
			luotFollowRepository.save(luotFollow);
			return true; // Đã follow thành công
		}
	}
	@GetMapping("/check-block")
	public boolean checkBlock(@RequestParam Long userId, @RequestParam Long otherUserId) {
		return blockUserService.checkBlock(userId, otherUserId);
	}


	// Unfollow user (hủy theo dõi)
	public boolean unfollow(Long userId, Long userFollowId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User không tồn tại."));
		User currentUser = userRepository.findById(userFollowId)
				.orElseThrow(() -> new RuntimeException("Chưa login."));

		// Tạo ID cho bảng trung gian
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);

		if (luotFollowRepository.existsById(luotFollowId)) {
			// Nếu đã follow, xóa bản ghi
			luotFollowRepository.deleteById(luotFollowId);
			return false; // Trả về trạng thái "không follow"
		}
		return true; // Nếu không có bản ghi, tức là chưa follow, trả về true
	}
	@PostMapping("/{userId}/toggleFollow")
	public ResponseEntity<?> toggleFollow(
			@PathVariable("userId") Long userId,
			@RequestParam Long userFollowId) {
		if (userId == null || userFollowId == null) {
			return ResponseEntity.badRequest().body("UserId và UserFollowId không được để trống.");
		}

		try {
			// Thay đổi trạng thái Follow
			boolean isFollowing = luotFollowService.toggleFollow(userId, userFollowId);

			// Gửi thông báo trạng thái mới qua WebSocket
			webSocketHandler.sendFollowStatusChange(userId, isFollowing);

			// Phản hồi trạng thái hiện tại (true: đang follow, false: hủy follow)
			Map<String, Object> response = new HashMap<>();
			response.put("isFollowing", isFollowing);
			response.put("message", isFollowing ? "Đã follow thành công." : "Đã hủy follow.");

			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
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
		Integer luotFollow = luotFollowRepository.demSoLuongNguoiDuocTheoDoi(id);
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
