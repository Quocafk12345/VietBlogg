package com.VietBlog.repository;

import com.VietBlog.entity.BlockUser_ID;
import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuotFollowRepository extends JpaRepository<LuotFollow, LuotFollowId> {

	@Query("SELECT COUNT(lf) FROM LuotFollow lf WHERE lf.userFollow.id = :userId")
	int demSoLuongNguoiDuocTheoDoi(@Param("userId") Long userId);

	// Đếm số lượng người dùng follow của một người dùng
	@Query("SELECT COUNT(lf) FROM LuotFollow lf WHERE lf.user.id = :userId")
	int demSoLuongNguoiTheoDoi(@Param("userId") Long userId);

	// Lấy danh sách người dùng mà một người dùng đang follow
	@Query("SELECT lf.userFollow FROM LuotFollow lf WHERE lf.user.id = :userId")
	List<User> layDSNguoiDuocTheoDoi(@Param("userId") Long userId);

	// Lấy danh sách người dùng đang follow một người dùng
	@Query("SELECT lf.user FROM LuotFollow lf WHERE lf.user.id = :userId")
	List<User> layDSNguoiTheoDoi(@Param("userId") Long userId);

}