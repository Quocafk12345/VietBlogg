package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Long> {

	// Tìm nhóm theo tên
	Nhom findByTen(String ten);

	// Tìm nhóm theo id người tạo (userId)
	@Query("SELECT n FROM Nhom n JOIN FETCH n.thanhVien tv WHERE tv.id.userId = :userId AND tv.vaiTro = 'Quản trị viên'")
	List<Nhom> findNhomByNguoiTao(@Param("userId") Long userId);

	// Tìm nhóm theo userId người dùng
	@Query("SELECT n FROM Nhom n JOIN FETCH n.thanhVien tv WHERE tv.id.userId = :userId")
	List<Nhom> findNhomByUserId(@Param("userId") Long userId);

	// Đếm số lượng thành viên của một nhóm
	@Query("SELECT COUNT(tv.id.userId) FROM ThanhVien tv WHERE tv.id.idNhom = :nhomId")
	int countThanhVienByNhomId(@Param("nhomId") Long nhomId);

	// Lấy danh sách bài viết của một nhóm
	@Query("SELECT bv FROM BaiViet bv JOIN FETCH bv.nhom n WHERE n.id = :nhomId")
	List<BaiViet> findBaiVietByNhomId(@Param("nhomId") Long nhomId);
}