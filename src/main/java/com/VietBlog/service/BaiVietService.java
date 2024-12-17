package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.repository.BaiVietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BaiVietService {

	private final BaiVietRepository baiVietRepository;

	@Autowired
	public BaiVietService(BaiVietRepository baiVietRepository){
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

	// Thêm bài viết mới Tests
	// Thay đổi kiểu trả về từ void thành BaiViet
	@Transactional
	public BaiViet themBaiViet(BaiViet baiViet) {
		baiViet.setNgayTao(Timestamp.from(Instant.now()));
		return baiVietRepository.save(baiViet);
	}

	// Cập nhật bài viết
	@Transactional
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
	@Transactional
	public void xoaBaiViet(Long id) {
		baiVietRepository.deleteById(id);
	}

	// Lấy bài viết theo ID
	public BaiViet getBaiVietById(Long id) {
		return baiVietRepository.findById(id).orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
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

	public List<BaiViet> getBaiVietByNhomIdAndUserId(Long nhomId, Long userId) {
		return baiVietRepository.findByNhomIdAndUserId(nhomId, userId);
	}

//	public List<Map<String, Object>> countPostsByMonthAllUsers() {
//		List<Object[]> results = baiVietRepository.countPostsByMonthAllUsers();
//		List<Map<String, Object>> counts = new ArrayList<>();
//		for (Object[] result : results) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("month", result[0]);
//			map.put("count", result[1]);
//			counts.add(map);
//		}
//		return counts;
//	}
public List<Map<String, Object>> countPostsByMonthAllUsers() {
	List<Object[]> results = baiVietRepository.countPostsByMonthAllUsers();
	List<Map<String, Object>> counts = new ArrayList<>();
	for (Object[] result : results) {
		Map<String, Object> map = new HashMap<>();
		map.put("month", result[0]);
		map.put("year", result[1]); // Thêm năm vào map
		map.put("count", result[2]); // count bây giờ là result[2]
		counts.add(map);
	}
	return counts;
}
}