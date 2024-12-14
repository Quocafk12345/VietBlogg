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

    List<BlockUserNhom> findByNhom_Id(Long nhomId);

    //phuong thuc xoa nhom
    void deleteAllByNhom_Id(Long nhomId);

}