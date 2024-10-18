package com.VietBlog.repository;

import com.VietBlog.entity.DS_LuatNhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DS_LuatNhom_Repository extends JpaRepository<DS_LuatNhom, Long> {

	// Tìm tất cả luật của một nhóm theo idNhom
	List<DS_LuatNhom> findByNhom_Id(Long idNhom);

	// Tìm luật theo tên
	DS_LuatNhom findByTen(String ten);

	// Tìm luật theo nội dung
	DS_LuatNhom findByNoiDung(String noiDung);

	// Đếm số lượng luật của một nhóm theo idNhom
	int countByNhom_Id(Long idNhom);
}