package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet_ID;
import com.VietBlog.entity.User;
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
	private final LuotLike_BaiViet_Repository luotLike_BaiViet_Repository;

	@Autowired
	public LuotLike_BaiViet_Service(LuotLike_BaiViet_Repository luotLikeRepository, UserRepository userRepository, BaiVietRepository baiVietRepository, LuotLike_BaiViet_Repository luotLike_BaiViet_Repository) {
		this.luotLikeRepository = luotLikeRepository;
		this.userRepository = userRepository;
		this.baiVietRepository = baiVietRepository;
		this.luotLike_BaiViet_Repository = luotLike_BaiViet_Repository;
	}

	public boolean toggleLike(Long idBaiViet, Long userId) {
		// Kiểm tra người dùng và bài viết có tồn tại không
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User không tồn tại."));
		BaiViet baiViet = baiVietRepository.findById(idBaiViet)
				.orElseThrow(() -> new RuntimeException("Bài viết không tồn tại."));

		// Tạo ID cho bảng trung gian
		LuotLike_BaiViet_ID luotLikeId = new LuotLike_BaiViet_ID(userId, idBaiViet);

		if (luotLike_BaiViet_Repository.existsById(luotLikeId)) {
			// Nếu đã like, xóa bản ghi
			luotLike_BaiViet_Repository.deleteById(luotLikeId);
			return false; // Trả về trạng thái "không like"
		} else {
			// Nếu chưa like, thêm bản ghi
			LuotLike_BaiViet luotLikeBaiViet = new LuotLike_BaiViet();
			luotLikeBaiViet.setId(luotLikeId);
			luotLikeBaiViet.setUser(user);
			luotLikeBaiViet.setBaiViet(baiViet);

			luotLike_BaiViet_Repository.save(luotLikeBaiViet);
			return true; // Trả về trạng thái "đã like"
		}
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
	public boolean kiemTraLikeBaiViet(Long userId, Long baiVietId) {
		LuotLike_BaiViet_ID id = new LuotLike_BaiViet_ID(userId, baiVietId);
		return luotLikeRepository.existsById(id);
	}

	// Lấy danh sách bài viết mà người dùng đã like
	public List<BaiViet> layDanhSachBaiVietDaLike(Integer userId) {
		return luotLikeRepository.findBaiVietLikedByUserId(userId);
	}
}
