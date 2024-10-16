package com.VietBlog.repository;

import com.VietBlog.entity.ThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, Integer> {
    List<ThanhVien> findByUser_Id(Integer UserId);

}
