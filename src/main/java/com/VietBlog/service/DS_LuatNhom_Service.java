package com.VietBlog.service;

import com.VietBlog.entity.DS_LuatNhom;
import com.VietBlog.repository.DS_LuatNhom_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DS_LuatNhom_Service {

	private final DS_LuatNhom_Repository dsLuatNhomRepository;

	@Autowired
	public DS_LuatNhom_Service(DS_LuatNhom_Repository dsLuatNhomRepository) {
		this.dsLuatNhomRepository = dsLuatNhomRepository;
	}

	// Lấy danh sách tất cả luật của một nhóm
	public List<DS_LuatNhom> layDSLuatNhom(Long nhomId) {
		return dsLuatNhomRepository.findByNhom_Id(nhomId);
	}

	// Thêm luật mới cho một nhóm
	public DS_LuatNhom themLuatNhom(DS_LuatNhom dsLuatNhom) {
		// Thực hiện các kiểm tra validation (nếu cần)
		return dsLuatNhomRepository.save(dsLuatNhom);
	}

	// Cập nhật luật của một nhóm
	public DS_LuatNhom capNhatLuatNhom(Long luatId, DS_LuatNhom dsLuatNhom) {
		// Kiểm tra xem luật có tồn tại không
		if (!dsLuatNhomRepository.existsById(luatId)) {
			throw new RuntimeException("Luật nhóm không tồn tại");
		}
		// Thực hiện các kiểm tra validation (nếu cần)
		dsLuatNhom.setId(luatId); // Đảm bảo ID không bị thay đổi
		return dsLuatNhomRepository.save(dsLuatNhom);
	}

	// Xóa luật của một nhóm
	public void xoaLuatNhom(Long luatId) {
		dsLuatNhomRepository.deleteById(luatId);
	}

	// Lấy luật theo tên
	public DS_LuatNhom layLuatTheoTen(String ten) {
		return dsLuatNhomRepository.findByTen(ten);
	}

	// Lấy luật theo nội dung
	public DS_LuatNhom layLuatTheoNoiDung(String noiDung) {
		return dsLuatNhomRepository.findByNoiDung(noiDung);
	}

	// Đếm số lượng luật của một nhóm
	public int demSoLuongLuatNhom(Long nhomId) {
		return dsLuatNhomRepository.countByNhom_Id(nhomId);
	}

}