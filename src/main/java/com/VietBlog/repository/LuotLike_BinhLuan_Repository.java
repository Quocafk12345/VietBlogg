package com.VietBlog.repository;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.entity.LuotLike_BinhLuan;
import com.VietBlog.entity.LuotLike_BinhLuan_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuotLike_BinhLuan_Repository extends JpaRepository<LuotLike_BinhLuan, LuotLike_BinhLuan_ID> {

	// Kiểm tra xem một người dùng đã like một bình luận chưa
	boolean existsById(@NonNull LuotLike_BinhLuan_ID id);

	// Đếm số lượng lượt like của một bình luận
	int countByIdBinhLuan(Long idBinhLuan);

	// Lấy danh sách bình luận mà một người dùng đã like
	@Query("SELECT llbl.idBinhLuan FROM LuotLike_BinhLuan llbl WHERE llbl.id.userId = :userId")
	List<BinhLuan> findBinhLuanByUserId(@Param("userId") Long userId);

	// Xóa một lượt like bình luận
	void deleteById(@NonNull LuotLike_BinhLuan_ID id);
}