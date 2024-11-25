package com.VietBlog.controller;

import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
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
import java.util.Map;

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
		BlockUserID blockUserID = new BlockUserID(userId,blockUserId);
		boolean isBlocking = blockUserRepository.existsById(blockUserID);

		Map<String, Boolean> response = new HashMap<>();
		response.put("isBlocking", isBlocking);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{userId}/checkFollowStatus")
	public ResponseEntity<Map<String, Boolean>> checkFollowStatus(@PathVariable Long userId, @RequestParam Long userFollowId) {
		LuotFollowId followId = new LuotFollowId(userFollowId, userId);
		boolean isFollowing = luotFollowRepository.existsById(followId);

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
}