package com.VietBlog.repository;

import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, ThanhVienId> {
    List<ThanhVien> findByUser_Id(Long userId);
    // Tìm theo idNhom
    List<ThanhVien> findByNhom_Id(Long mhomId);
    void deleteAllByNhom_Id(Long mhomId);

    @Query("SELECT tv FROM ThanhVien tv JOIN tv.user u WHERE tv.nhom.id = :nhomId")
    List<ThanhVien> findThanhVienByNhomId(@Param("nhomId") Long nhomId);

}
