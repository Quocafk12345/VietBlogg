package com.VietBlog.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.repository.BaiVietRepository;
import java.util.List;
@Service
public class BaiVietService {

	@Autowired
	private BaiVietRepository baiVietRepository;


	public int getTotalBaiVietByUserId(Long userId) {
		return baiVietRepository.countBaiVietByUserId(userId);
	}

	// Nếu bạn có phương thức này để đếm tổng số bài viết không dựa trên userId
	public int getTotalBaiViet() {
		return (int) baiVietRepository.count();
	}
	public List<BaiViet> getBaiVietByUserId(Long userId) {
		return baiVietRepository.findByUserId(userId);
	}
	public List<BaiViet> getAllBaiViet() {
		return baiVietRepository.findAll();
	}
}

