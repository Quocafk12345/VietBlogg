package com.VietBlog.repository;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet_ID;
import com.VietBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuotLike_BaiViet_Repository extends JpaRepository<LuotLike_BaiViet, LuotLike_BaiViet_ID> {
    // đếm số lượt like bài viết
    Integer countLuotLike_BaiVietByBaiVietId(Long idBaiViet);
    // đếm số lượt like tổng mà người dùng nhận trong tất cả bài đăng
    Integer countLuotLike_BaiVietByUserId(Long userId);

    // Lấy danh sách bài viết mà người dùng đã like
    @Query("SELECT ll.baiViet FROM LuotLike_BaiViet ll WHERE ll.id.userId = :userId")
    List<BaiViet> findBaiVietLikedByUserId(@Param("userId") Integer userId);

    // Kiểm tra xem một người dùng đã like một bài viết chưa
    boolean existsById(@NonNull LuotLike_BaiViet_ID id);
    void deleteById(LuotLike_BaiViet_ID id);

    // ... (Các phương thức khác)

    // Lấy tổng số lượt thích
    @Query("SELECT COUNT(ll) FROM LuotLike_BaiViet ll")
    Long tongLuotThich();
}
