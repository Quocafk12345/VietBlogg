package com.VietBlog.repository;

import com.VietBlog.entity.BlockUser_ID;
import com.VietBlog.entity.LuotFollow;
import com.VietBlog.entity.LuotFollowId;
import com.VietBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuotFollowRepository extends JpaRepository<LuotFollow, LuotFollowId> {

	// Kiểm tra xem một người dùng đã follow người dùng khác chưa
	boolean existsById(@NonNull LuotFollowId luotFollowId);

	@Query("SELECT COUNT(lf) FROM LuotFollow lf WHERE lf.userFollow.id = :userId")
	int countFollowingByUserId(@Param("userId") Long userId);

	// Đếm số lượng người dùng follow của một người dùng
	@Query("SELECT COUNT(lf) FROM LuotFollow lf WHERE lf.user.id = :userId")
	int countFollowersByUserId(@Param("userId") Long userId);

	// Lấy danh sách người dùng mà một người dùng đang follow
	@Query("SELECT lf.userFollow FROM LuotFollow lf WHERE lf.user.id = :userId")
	List<User> findFollowingByUserId(@Param("userId") Long userId);

	// Lấy danh sách người dùng đang follow một người dùng
	@Query("SELECT lf.user FROM LuotFollow lf WHERE lf.user.id = :userId")
	List<User> findFollowersByUserId(@Param("userId") Long userId);

	// Xóa một mối quan hệ follow
	void deleteById(@NonNull LuotFollowId luotFollowId);

}