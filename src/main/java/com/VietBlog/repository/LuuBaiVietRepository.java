package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuuBaiVietRepository extends JpaRepository<LuuBaiViet, LuuBaiViet_ID> {

	// Kiểm tra xem một người dùng đã lưu một bài viết chưa
	boolean existsById(@NonNull LuuBaiViet_ID id);

	// Đếm số lượng bài viết mà một người dùng đã lưu
	@Query("SELECT COUNT(lbv.id.userId) FROM LuuBaiViet lbv WHERE lbv.id.userId = :userId")
	int countByUserId(@Param("userId") Long userId);

	// Lấy danh sách bài viết mà một người dùng đã lưu
	@Query("SELECT lbv.baiViet FROM LuuBaiViet lbv WHERE lbv.id.userId = :userId")
	List<BaiViet> findBaiVietByUserId(@Param("userId") Long userId);

	// Xóa một bài viết đã lưu
	void deleteById(@NonNull LuuBaiViet_ID id);

}