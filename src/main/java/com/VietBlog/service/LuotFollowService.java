package com.VietBlog.service;

import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import com.VietBlog.repository.LuotFollowRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// Follow một người dùng
	@Transactional
	public LuotFollow follow(Long userId, Long userFollowId) {
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);
		if (luotFollowRepository.existsById(luotFollowId)) {
			throw new RuntimeException("Người dùng đã follow người dùng này rồi");
		}
		LuotFollow luotFollow = new LuotFollow(luotFollowId,
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				userRepository.findById(userFollowId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng được follow")));
		return luotFollowRepository.save(luotFollow);
	}

	// Unfollow một người dùng
	@Transactional
	public void unfollow(Long userId, Long userFollowId) {
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);
		if (!luotFollowRepository.existsById(luotFollowId)) {
			throw new RuntimeException("Người dùng chưa follow người dùng này");
		}
		luotFollowRepository.deleteById(luotFollowId);
	}

	// Kiểm tra xem một người dùng đã follow người dùng khác chưa
	public boolean daFollow(Long userId, Long userFollowId) {
		LuotFollowId luotFollowId = new LuotFollowId(userId, userFollowId);
		return luotFollowRepository.existsById(luotFollowId);
	}

	// Đếm số lượng người dùng mà một người dùng đang follow
	public int demSoLuongFollowing(Long userId) {
		return luotFollowRepository.countFollowingByUserId(userId);
	}

	// Đếm số lượng người dùng đang follow một người dùng
	public int demSoLuongFollowers(Long userId) {
		return luotFollowRepository.countFollowersByUserId(userId);
	}

	// Lấy danh sách người dùng mà một người dùng đang follow
	public List<User> layDanhSachFollowing(Long userId) {
		return luotFollowRepository.findFollowingByUserId(userId);
	}

	// Lấy danh sách người dùng đang follow một người dùng
	public List<User> layDanhSachFollowers(Long userId) {
		return luotFollowRepository.findFollowersByUserId(userId);
	}
}