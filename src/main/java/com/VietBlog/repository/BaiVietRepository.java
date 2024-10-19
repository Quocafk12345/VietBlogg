package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    // Đếm tổng số lượng bài viết
    @Query("SELECT COUNT(b.id) FROM BaiViet b")
    Integer countTotalBaiViet();
    @Query("SELECT COUNT(b) FROM BaiViet b WHERE b.user.id = :userId")
    Integer countBaiVietByUserId(@Param("userId") Long userId);
    @Query("SELECT b FROM BaiViet b WHERE b.user.id = :userId")
    List<BaiViet> findByUserId(@Param("userId") Long userId);

    @Query("SELECT bv FROM BaiViet bv WHERE "
            + "(bv.tieuDe LIKE %:keyword% OR bv.noiDung LIKE %:keyword%)"
            + " AND (bv.user.tenNguoiDung LIKE %:keyword%)")
    List<BaiViet> findByKeyword(@Param("keyword") String keyword);
}