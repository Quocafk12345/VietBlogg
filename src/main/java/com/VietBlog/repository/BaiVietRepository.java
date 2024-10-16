package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {
    // Đếm tổng số lượng bài viết
    @Query("SELECT COUNT(b.idBaiViet) FROM BaiViet b")
    int countTotalBaiViet();
    @Query("SELECT COUNT(b) FROM BaiViet b WHERE b.user.id = :userId")
    int countBaiVietByUserId(@Param("userId") Long userId);
    @Query("SELECT b FROM BaiViet b WHERE b.user.id = :userId")
    List<BaiViet> findByUserId(@Param("userId") Long userId);

}