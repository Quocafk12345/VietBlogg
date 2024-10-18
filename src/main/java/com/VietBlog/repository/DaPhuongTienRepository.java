package com.VietBlog.repository;

import com.VietBlog.entity.DaPhuongTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaPhuongTienRepository extends JpaRepository<DaPhuongTien, Long> {
	// Các phương thức truy vấn (nếu cần)
	List<DaPhuongTien> findByBaiViet_Id(Long idBaiViet);

	// Tìm tất cả DaPhuongTien theo đường dẫn
	DaPhuongTien findByDuongDan(String duongDan);

	int countByBaiViet_Id(Long idBaiViet);

	void deleteAllByBaiViet_Id(Long idBaiViet);
}