package com.VietBlog.service;

import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LuotFollowService {

	private final LuotFollowRepository luotFollowRepository;

	private final UserRepository userRepository;

	@Autowired
	public LuotFollowService(LuotFollowRepository luotFollowRepository, UserRepository userRepository) {
		this.luotFollowRepository = luotFollowRepository;
		this.userRepository = userRepository;
	}

	public boolean toggleFollow(Long userId, Long userFollowId) {
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
		} else {
			// Nếu chưa follow, thêm bản ghi
			LuotFollow luotFollow = new LuotFollow();
			luotFollow.setId(luotFollowId);
			luotFollow.setUser(user);
			luotFollow.setUserFollow(currentUser);
			luotFollowRepository.save(luotFollow);
			return true; // Trả về trạng thái "đã follow"
		}
	}

	// Kiểm tra xem một người dùng đã follow người dùng khác chưa
	public boolean kiemTraFollow(Long userId, Long userFollowId) {
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);
		return luotFollowRepository.existsById(luotFollowId);
	}

	// Đếm số lượng người dùng mà một người dùng đang follow
	public int demSoLuongFollowing(Long userId) {
		return luotFollowRepository.demSoLuongNguoiDuocTheoDoi(userId);
	}

	// Đếm số lượng người dùng đang follow một người dùng
	public int demSoLuongFollowers(Long userId) {
		return luotFollowRepository.demSoLuongNguoiTheoDoi(userId);
	}

	// Lấy danh sách người dùng mà một người dùng đang follow
	public List<User> layDanhSachFollowing(Long userId) {
		return luotFollowRepository.layDSNguoiDuocTheoDoi(userId);
	}

	// Lấy danh sách người dùng đang follow một người dùng
	public List<User> layDanhSachFollowers(Long userId) {
		return luotFollowRepository.layDSNguoiTheoDoi(userId);
	}
}