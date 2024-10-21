package com.VietBlog.repository;

import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, ThanhVienId> {
    List<ThanhVien> findByUser_Id(Long UserId);
    List<ThanhVien> findByNhom_Id(Long NhomId);
}
