package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {
    @Query("SELECT bv FROM BaiViet bv WHERE "
            + "(bv.tieuDe LIKE %:keyword% OR bv.noiDung LIKE %:keyword%)"
            + " AND (bv.user.tenNguoiDung LIKE %:keyword%)")
    public List<BaiViet> findByKeyword(@Param("keyword") String keyword);

}
