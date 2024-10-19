package com.VietBlog.service;

import com.VietBlog.controller.BaiVietController;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.repository.BaiVietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaiVietService {

	private final BaiVietRepository baiVietRepository;

	@Autowired
	private BaiVietService(BaiVietRepository baiVietRepository){
		this.baiVietRepository = baiVietRepository;
	}

	// Lấy tất cả bài viết
	public List<BaiViet> getAllBaiViet() {
		return baiVietRepository.findAll();
	}

	// Lấy bài viết theo User_Id
	public List<BaiViet> getBaiVietByUserId(Long userId) {
		return baiVietRepository.findByUserId(userId);
	}

	// Thêm bài viết mới
	public BaiViet themBaiViet(BaiViet baiViet) {
		return baiVietRepository.save(baiViet);
	}

	// Cập nhật bài viết
	public BaiViet capNhatBaiViet(Long id, BaiViet baiViet) {
		Optional<BaiViet> optionalBaiViet = baiVietRepository.findById(id);
		if (optionalBaiViet.isPresent()) {
			BaiViet existingBaiViet = optionalBaiViet.get();
			existingBaiViet.setTieuDe(baiViet.getTieuDe());
			existingBaiViet.setNoiDung(baiViet.getNoiDung());
			return baiVietRepository.save(existingBaiViet);
		} else {
			throw new RuntimeException("Bài viết không tồn tại");
		}
	}

	// Xóa bài viết
	public void xoaBaiViet(Long id) {
		baiVietRepository.deleteById(id);
	}

	// Lấy bài viết theo ID
	public BaiViet getBaiVietById(Long id) {
		return baiVietRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
	}

	// Lấy bài viết theo keyword
	public List<BaiViet> getBaiVietByKeyword(String keyword) {
		return baiVietRepository.findByKeyword(keyword);
	}

	// Kiểm tra sự tồn tại của bài viết
	public boolean existsById(Long id) {
		return baiVietRepository.existsById(id);
	}

	// Đếm tổng số lượng bài viết
	public Integer countTotalBaiViet() {
		return baiVietRepository.countTotalBaiViet();
	}

	// Đếm số lượng bài viết theo User_Id
	public Integer countBaiVietByUserId(Long userId) {
		return baiVietRepository.countBaiVietByUserId(userId);
	}



}