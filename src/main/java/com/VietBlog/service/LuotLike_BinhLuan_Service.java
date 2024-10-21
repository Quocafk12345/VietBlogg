package com.VietBlog.service;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.entity.LuotLike_BinhLuan;
import com.VietBlog.entity.LuotLike_BinhLuan_ID;
import com.VietBlog.repository.BinhLuanRepository;
import com.VietBlog.repository.LuotLike_BinhLuan_Repository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LuotLike_BinhLuan_Service {

	private final LuotLike_BinhLuan_Repository luotLikeBinhLuanRepository;
	private final UserRepository userRepository;
	private final BinhLuanRepository binhLuanRepository;

	@Autowired
	public LuotLike_BinhLuan_Service(LuotLike_BinhLuan_Repository luotLikeBinhLuanRepository, UserRepository userRepository, BinhLuanRepository binhLuanRepository) {
		this.luotLikeBinhLuanRepository = luotLikeBinhLuanRepository;
		this.userRepository = userRepository;
		this.binhLuanRepository = binhLuanRepository;
	}

	// Thêm lượt like cho bình luận
	@Transactional
	public LuotLike_BinhLuan themLuotLike(Long userId, Long binhLuanId) {
		LuotLike_BinhLuan_ID id = new LuotLike_BinhLuan_ID(userId, binhLuanId);
		if (luotLikeBinhLuanRepository.existsById(id)) {
			throw new RuntimeException("Người dùng đã like bình luận này rồi");
		}
		LuotLike_BinhLuan luotLike =    new LuotLike_BinhLuan(id,
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				binhLuanRepository.findById(binhLuanId).orElseThrow(() -> new RuntimeException("Không tìm thấy bình luận")));
		return luotLikeBinhLuanRepository.save(luotLike);
	}

	// Xóa lượt like khỏi bình luận
	@Transactional
	public void xoaLuotLike(Long userId, Long binhLuanId) {
		LuotLike_BinhLuan_ID id = new LuotLike_BinhLuan_ID(userId, binhLuanId);
		if (!luotLikeBinhLuanRepository.existsById(id)) {
			throw new RuntimeException("Người dùng chưa like bình luận này");
		}
		luotLikeBinhLuanRepository.deleteById(id);
	}

	// Kiểm tra xem một người dùng đã like một bình luận chưa
	public boolean daLikeBinhLuan(Long userId, Long binhLuanId) {
		LuotLike_BinhLuan_ID id = new LuotLike_BinhLuan_ID(userId, binhLuanId);
		return luotLikeBinhLuanRepository.existsById(id);
	}

	// Đếm số lượng lượt like của một bình luận
	public int demLuotLikeCuaBinhLuan(Long binhLuanId) {
		return luotLikeBinhLuanRepository.countByBinhLuan_Id(binhLuanId);
	}

	// Lấy danh sách bình luận mà một người dùng đã like
	public List<BinhLuan> layDanhSachBinhLuanDaLike(Long userId) {
		return luotLikeBinhLuanRepository.findBinhLuanByUserId(userId);
	}

}