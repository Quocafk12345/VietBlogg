package com.VietBlog.service;

import com.VietBlog.entity.DaPhuongTien;
import com.VietBlog.repository.DaPhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DaPhuongTienService {

	@Autowired
	private DaPhuongTienRepository daPhuongTienRepository;

	public List<DaPhuongTien> getDaPhuongTienByBaiVietId(Long idBaiViet) {
		return daPhuongTienRepository.findByBaiViet_Id(idBaiViet);
	}

	public DaPhuongTien getDaPhuongTienByDuongDan(String duongDan) {
		return daPhuongTienRepository.findByDuongDan(duongDan);
	}

	public int countDaPhuongTienByBaiVietId(Long idBaiViet) {
		return daPhuongTienRepository.countByBaiViet_Id(idBaiViet);
	}

	public void deleteAllDaPhuongTienByBaiVietId(Long idBaiViet) {
		daPhuongTienRepository.deleteAllByBaiViet_Id(idBaiViet);
	}

}