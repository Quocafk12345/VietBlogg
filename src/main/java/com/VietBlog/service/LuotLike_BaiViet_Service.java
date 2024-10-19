package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet_ID;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.LuotLike_BaiViet_Repository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LuotLike_BaiViet_Service {

	private final LuotLike_BaiViet_Repository luotLikeRepository;
	private final UserRepository userRepository;
	private final BaiVietRepository baiVietRepository;

	@Autowired
	public LuotLike_BaiViet_Service(LuotLike_BaiViet_Repository luotLikeRepository, UserRepository userRepository, BaiVietRepository baiVietRepository) {
		this.luotLikeRepository = luotLikeRepository;
		this.userRepository = userRepository;
		this.baiVietRepository = baiVietRepository;
	}

	// Thêm lượt like
	@Transactional
	public LuotLike_BaiViet themLuotLike(Long userId, Long baiVietId) {
		LuotLike_BaiViet_ID id = new LuotLike_BaiViet_ID(userId, baiVietId);
		if (luotLikeRepository.existsById(id)) {
			throw new RuntimeException("Người dùng đã like bài viết này rồi");
		}
		LuotLike_BaiViet luotLike = new LuotLike_BaiViet(id,
				userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				baiVietRepository.findById(baiVietId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết")));
		return luotLikeRepository.save(luotLike);
	}

	// Xóa lượt like
	@Transactional
	public void xoaLuotLike(Long userId, Long baiVietId) {
		LuotLike_BaiViet_ID id = new LuotLike_BaiViet_ID(userId, baiVietId);
		if (!luotLikeRepository.existsById(id)) {
			throw new RuntimeException("Người dùng chưa like bài viết này");
		}
		luotLikeRepository.deleteById(id);
	}

	// Đếm số lượt like theo bài viết
	public int demLuotLikeTheoBaiViet(Long baiVietId) {
		return luotLikeRepository.countLuotLike_BaiVietByBaiVietId(baiVietId);
	}

	// Đếm số lượt like theo người dùng
	public int demLuotLikeTheoNguoiDung(Long userId) {
		return luotLikeRepository.countLuotLike_BaiVietByUserId(userId);
	}

	// Kiểm tra xem một người dùng đã like một bài viết chưa
	public boolean daLikeBaiViet(Long userId, Long baiVietId) {
		LuotLike_BaiViet_ID id = new LuotLike_BaiViet_ID(userId, baiVietId);
		return luotLikeRepository.existsById(id);
	}

	// Lấy danh sách bài viết mà người dùng đã like
	public List<BaiViet> layDanhSachBaiVietDaLike(Integer userId) {
		return luotLikeRepository.findBaiVietLikedByUserId(userId);
	}
}
