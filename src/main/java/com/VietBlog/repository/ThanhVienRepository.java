package com.VietBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, Integer> {
    
}
