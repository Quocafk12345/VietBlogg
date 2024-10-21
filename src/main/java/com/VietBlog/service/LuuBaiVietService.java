package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.LuuBaiVietRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LuuBaiVietService {

	private final LuuBaiVietRepository luuBaiVietRepository;
	private final UserRepository userRepository;
	private final BaiVietRepository baiVietRepository;

	@Autowired
	public LuuBaiVietService(LuuBaiVietRepository luuBaiVietRepository, UserRepository userRepository, BaiVietRepository baiVietRepository) {
		this.luuBaiVietRepository = luuBaiVietRepository;
		this.userRepository = userRepository;
		this.baiVietRepository = baiVietRepository;
	}

	// Lưu bài viết
	@Transactional
	public LuuBaiViet luuBaiViet(Long userId, Long baiVietId) {
		LuuBaiViet_ID id = new LuuBaiViet_ID(userId, baiVietId);
		if (luuBaiVietRepository.existsById(id)) {
			throw new RuntimeException("Người dùng đã lưu bài viết này rồi");
		}
		LuuBaiViet luuBaiViet = new LuuBaiViet(id,
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				baiVietRepository.findById(baiVietId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết")));
		return luuBaiVietRepository.save(luuBaiViet);
	}

	// Bỏ lưu bài viết
	@Transactional
	public void boLuuBaiViet(Long userId, Long baiVietId) {
		LuuBaiViet_ID id = new LuuBaiViet_ID(userId, baiVietId);
		if (!luuBaiVietRepository.existsById(id)) {
			throw new RuntimeException("Người dùng chưa lưu bài viết này");
		}
		luuBaiVietRepository.deleteById(id);
	}

	// Kiểm tra xem một người dùng đã lưu một bài viết chưa
	public boolean daLuuBaiViet(Long userId, Long baiVietId) {
		LuuBaiViet_ID id = new LuuBaiViet_ID(userId, baiVietId);
		return luuBaiVietRepository.existsById(id);
	}

	// Đếm số lượng bài viết mà một người dùng đã lưu
	public int demSoLuongBaiVietDaLuu(Long userId) {
		return luuBaiVietRepository.countByUserId(userId);
	}

	// Lấy danh sách bài viết mà một người dùng đã lưu
	public List<BaiViet> layDanhSachBaiVietDaLuu(Long userId) {
		return luuBaiVietRepository.findBaiVietByUserId(userId);
	}

	// ... các phương thức khác
}