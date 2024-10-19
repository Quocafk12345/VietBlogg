package com.VietBlog.repository;

import com.VietBlog.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {

	List<ThongBao> findByUserId(Long userId);

	// Tìm tất cả thông báo chưa đọc của một người dùng
	@Query("SELECT tb FROM ThongBao tb WHERE tb.user.id = :userId AND tb.daDoc = false")
	List<ThongBao> findUnreadByUserId(@Param("userId") Long userId);

	// Đếm số lượng thông báo chưa đọc của một người dùng
	@Query("SELECT COUNT(tb) FROM ThongBao tb WHERE tb.user.id = :userId AND tb.daDoc = false")
	int countUnreadByUserId(@Param("userId") Long userId);

}
