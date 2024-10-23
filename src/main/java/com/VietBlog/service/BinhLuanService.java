package com.VietBlog.service;

import com.VietBlog.constraints.BinhLuan.Level_Binh_Luan;
import com.VietBlog.entity.BinhLuan;
import com.VietBlog.entity.LuotLike_BinhLuan;
import com.VietBlog.repository.BinhLuanRepository;
import com.VietBlog.repository.LuotLike_BinhLuan_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinhLuanService {

	private final BinhLuanRepository binhLuanRepository;
	private final LuotLike_BinhLuan_Repository luotLike_BinhLuan_Repository;

	@Autowired
	public BinhLuanService(BinhLuanRepository binhLuanRepository, LuotLike_BinhLuan_Repository luotLike_BinhLuan_Repository) {
		this.binhLuanRepository = binhLuanRepository;
		this.luotLike_BinhLuan_Repository = luotLike_BinhLuan_Repository;
	}

	// Thêm bình luận mới
	@Transactional
	public BinhLuan themBinhLuan(BinhLuan binhLuan) {
		if (binhLuan.getLevel() != null && binhLuan.getLevel().compareTo(Level_Binh_Luan.CAP_2) > 0) {
			throw new IllegalArgumentException("Level bình luận không hợp lệ (tối đa là 2)");
		}

		// Nếu là bình luận con, kiểm tra level của bình luận cha
		if (binhLuan.getBinhLuanCha() != null) {
			BinhLuan binhLuanCha = binhLuanRepository.findById(binhLuan.getBinhLuanCha().getId())
					.orElseThrow(() -> new RuntimeException("Bình luận cha không tồn tại"));
			if (binhLuanCha.getLevel().compareTo(Level_Binh_Luan.CAP_2) >= 0) {
				binhLuan.setLevel(Level_Binh_Luan.CAP_2);
			}
			binhLuan.setLevel(Level_Binh_Luan.values()[binhLuanCha.getLevel().ordinal() + 1]);
		} else {
			binhLuan.setLevel(Level_Binh_Luan.BINH_LUAN_GOC); // Bình luận gốc
		}
		binhLuan.setNgayTao(Timestamp.from(Instant.now()));
		return binhLuanRepository.save(binhLuan);
	}

	// Lấy danh sách bình luận gốc (cấp 0) của bài viết
	public List<BinhLuan> layBinhLuanGoc(Long baiVietId) {
		return binhLuanRepository.findRootCommentsByBaiVietId(baiVietId);
	}

	// Lấy danh sách bình luận con (cấp 1) của một bình luận cha
	public List<BinhLuan> layBinhLuanCon(Long binhLuanChaId) {
		return binhLuanRepository.findByBinhLuanCha_Id(binhLuanChaId);
	}

	// Lấy danh sách bình luận theo người dùng
	public List<BinhLuan> getBinhLuanByUserId(Long userId) {
		return binhLuanRepository.findByUserId(userId);
	}

	// Xóa bình luận
	@Transactional
	public void xoaBinhLuan(Long binhLuanId) {
		// Xóa lượt like của bình luận gốc
		xoaLuotLikeCuaBinhLuanVaCon(binhLuanId);

		// Tìm tất cả bình luận con cấp 1 của bình luận cần xóa
		List<BinhLuan> binhLuanCap1 = binhLuanRepository.findByBinhLuanCha_Id(binhLuanId);

		for (BinhLuan blCap1 : binhLuanCap1) {
			xoaLuotLikeCuaBinhLuanVaCon(blCap1.getId());
			binhLuanRepository.deleteAll(binhLuanRepository.findByBinhLuanCha_Id(blCap1.getId()));
		}
		binhLuanRepository.deleteAll(binhLuanCap1);
		binhLuanRepository.deleteById(binhLuanId);
	}

	// Phương thức hỗ trợ xóa lượt like của bình luận và các bình luận con
	private void xoaLuotLikeCuaBinhLuanVaCon(Long binhLuanId) {
		// Xóa lượt like của bình luận hiện tại
		List<LuotLike_BinhLuan> luotLikes = luotLike_BinhLuan_Repository.findByBinhLuan_Id(binhLuanId);
		luotLike_BinhLuan_Repository.deleteAll(luotLikes);

		// Xóa lượt like của các bình luận con (đệ quy)
		List<BinhLuan> binhLuanCon = binhLuanRepository.findByBinhLuanCha_Id(binhLuanId);
		for (BinhLuan blCon : binhLuanCon) {
			xoaLuotLikeCuaBinhLuanVaCon(blCon.getId());
		}
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
