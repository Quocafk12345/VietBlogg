package com.VietBlog.repository;

import com.VietBlog.entity.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Integer> {
    Integer countBinhLuanByBaiVietId(Integer idBaiViet);
    List<BinhLuan> findBinhLuanByBaiViet_Id(Integer idBaiViet);
}
