package com.VietBlog.repository;

import com.VietBlog.entity.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    // Đếm số lượng bình luận theo bài viết (đã có sẵn)
    Integer countBinhLuanByBaiVietId(Long idBaiViet);

    // Tìm bình luận theo Id_BL_Cha
    List<BinhLuan> findByBinhLuanCha_Id(Long idBLCha);

    // Tìm bình luận theo Id_BaiViet (đã có sẵn, nhưng có thể sử dụng tên phương thức rõ ràng hơn)
    List<BinhLuan> findByBaiVietId(Long idBaiViet);

    // Tìm bình luận theo User_Id
    List<BinhLuan> findByUserId(Long userId);

    // Tìm bình luận gốc của một bài viết (level = 0)
    @Query("SELECT bl FROM BinhLuan bl WHERE bl.baiViet.id = :baiVietId AND bl.level = 0")
    List<BinhLuan> findRootCommentsByBaiVietId(@Param("baiVietId") Long baiVietId);

}
