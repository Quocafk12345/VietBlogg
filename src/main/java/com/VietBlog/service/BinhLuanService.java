package com.VietBlog.service;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.repository.BinhLuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BinhLuanService {

	private final BinhLuanRepository binhLuanRepository;

	@Autowired
	public BinhLuanService(BinhLuanRepository binhLuanRepository) {
		this.binhLuanRepository = binhLuanRepository;

	}

	// Thêm bình luận mới
	@Transactional
	public BinhLuan themBinhLuan(BinhLuan binhLuan) {
		if (binhLuan.getLevel() != null && binhLuan.getLevel() > 2) {
			throw new IllegalArgumentException("Level bình luận không hợp lệ (tối đa là 2)");
		}

		// Nếu là bình luận con, kiểm tra level của bình luận cha
		if (binhLuan.getBinhLuanCha() != null) {
			BinhLuan binhLuanCha = binhLuanRepository.findById(binhLuan.getBinhLuanCha().getId())
					.orElseThrow(() -> new RuntimeException("Bình luận cha không tồn tại"));
			if (binhLuanCha.getLevel() >= 2) {
				binhLuan.setLevel(2);
			}
			binhLuan.setLevel(binhLuanCha.getLevel() + 1);
		} else {
			binhLuan.setLevel(0); // Bình luận gốc
		}
		binhLuan.setNgayTao(LocalDate.now());
		return binhLuanRepository.save(binhLuan);
	}

	// Lấy danh sách bình luận theo bài viết
	public List<BinhLuan> getBinhLuanCuaBaiViet(Integer baiVietId) {
		return binhLuanRepository.findByBaiVietId(baiVietId);
	}

	// Lấy danh sách bình luận theo người dùng
	public List<BinhLuan> getBinhLuanByUserId(Integer userId) {
		return binhLuanRepository.findByUserId(userId);
	}

	// Xóa bình luận
	@Transactional
	public void xoaBinhLuan(Long binhLuanId) {
		binhLuanRepository.deleteById(binhLuanId);
	}

	// Cập nhật bình luận
	@Transactional
	public BinhLuan capNhatBinhLuan(Long binhLuanId, BinhLuan binhLuan) {
		BinhLuan existingBinhLuan = binhLuanRepository.findById(binhLuanId)
				.orElseThrow(() -> new RuntimeException("Bình luận không tồn tại"));
		existingBinhLuan.setNoiDung(binhLuan.getNoiDung());
		return binhLuanRepository.save(existingBinhLuan);
	}

}
