package com.VietBlog.repository;

import com.VietBlog.entity.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Integer> {

}
