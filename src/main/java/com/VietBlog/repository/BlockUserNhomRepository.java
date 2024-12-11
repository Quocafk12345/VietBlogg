package com.VietBlog.repository;

import com.VietBlog.entity.BlockUserNhom;
import com.VietBlog.entity.BlockUserNhomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlockUserNhomRepository extends JpaRepository<BlockUserNhom, BlockUserNhomId> {

    boolean existsByNhom_IdAndBlockedUser_Id(Long nhomId, Long blockedUserId);
    //Kiểm tra xem người dùng có bị chặn trong một khoảng thời gian cụ thể hay không
    boolean existsByUser_IdAndBlockedUser_IdAndNhom_IdAndNgayBatDauBeforeAndNgayKetThucAfter(Long userId, Long blockedUserId, Long nhomId, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc);

    //phuong thuc xoa nhom
    void deleteAllByNhom_Id(Long nhomId);

    //Tìm kiếm các bản ghi chặn theo khoảng thời gian
    List<BlockUserNhom> findByNgayBatDauBetween(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc);

    // Tìm kiếm các bản ghi chặn theo người dùng và nhóm, sắp xếp theo ngày bắt đầu
    List<BlockUserNhom> findByUser_IdAndNhom_IdOrderByNgayBatDauDesc(Long userId, Long nhomId);

//    @Query("SELECT CASE WHEN COUNT(bun) > 0 THEN true ELSE false END FROM BlockUserNhom bun WHERE bun.user.id = :userId AND bun.blockedUser.id = :blockedUserId AND bun.nhom.id = :nhomId AND bun.ngayKetThuc IS NULL")
//    boolean existsByUserAndBlockedUserAndNhomAndNgayKetThucIsNull(@Param("userId") Long userId, @Param("blockedUserId") Long blockedUserId, @Param("nhomId") Long nhomId);
@Query("SELECT CASE WHEN COUNT(bun) > 0 THEN true ELSE false END FROM BlockUserNhom bun WHERE bun.user.id = :userId AND bun.blockedUser.id = :blockedUserId AND bun.nhom.id = :nhomId AND bun.ngayKetThuc IS NULL")
boolean existsByUserAndBlockedUserAndNhomAndNgayKetThucIsNull(@Param("userId") Long userId, @Param("blockedUserId") Long blockedUserId, @Param("nhomId") Long nhomId);

}