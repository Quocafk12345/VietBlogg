package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    // Đếm tổng số lượng bài viết
    @Query("SELECT COUNT(b) FROM BaiViet b")
    Integer countTotalBaiViet();
    @Query("SELECT COUNT(b) FROM BaiViet b WHERE b.user.id = :userId")
    Integer countBaiVietByUserId(@Param("userId") Long userId);
    @Query("SELECT b FROM BaiViet b WHERE b.user.id = :userId")
    List<BaiViet> findByUserId(@Param("userId") Long userId);

    @Query("SELECT bv FROM BaiViet bv WHERE "
            + "(bv.tieuDe LIKE %:keyword% OR bv.noiDung LIKE %:keyword%)"
            + " AND (bv.user.tenNguoiDung LIKE %:keyword%)")
    List<BaiViet> findByKeyword(@Param("keyword") String keyword);

	@Query("SELECT b FROM BaiViet b WHERE b.trangThai = 'NHÁP' AND b.user.id = :userId")
	List<BaiViet> findBaiVietNhapCuaUser(@Param("userId") Long userId);

    void deleteAllByNhom_Id(Long nhomId);

    //Bài viết nhóm
    List<BaiViet> findByNhom_Id(Long nhomId);
    @Query("SELECT bv FROM BaiViet bv WHERE bv.nhom.id = :nhomId")
    List<BaiViet> findBaiVietByNhomId(@Param("nhomId") Long nhomId);

    //Bài vieest user trong nhóm
    @Query("SELECT bv FROM BaiViet bv WHERE bv.nhom.id = :nhomId AND bv.user.id = :userId")
    List<BaiViet> findByNhomIdAndUserId(@Param("nhomId") Long nhomId, @Param("userId") Long userId);
}