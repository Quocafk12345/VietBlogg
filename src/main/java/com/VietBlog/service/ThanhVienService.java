package com.VietBlog.service;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import com.VietBlog.repository.NhomRepository;
import com.VietBlog.repository.ThanhVienRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThanhVienService {

	private final ThanhVienRepository thanhVienRepository;
	private final NhomRepository nhomRepository;
	private final UserRepository userRepository;

	@Autowired
	public ThanhVienService(ThanhVienRepository thanhVienRepository, NhomRepository nhomRepository, UserRepository userRepository) {
		this.thanhVienRepository = thanhVienRepository;
		this.nhomRepository = nhomRepository;
		this.userRepository = userRepository;
	}

	// Thêm thành viên vào nhóm
	@Transactional
	public ThanhVien themThanhVien(Long nhomId, Long userId, String vaiTro) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng đã là thành viên của nhóm này");
		}
		ThanhVien thanhVien = new ThanhVien(thanhVienId,
				nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());
		return thanhVienRepository.save(thanhVien);
	}

	// Xóa thành viên khỏi nhóm
	@Transactional
	public void xoaThanhVien(Long nhomId, Long userId) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (!thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng không phải là thành viên của nhóm này");
		}
		thanhVienRepository.deleteById(thanhVienId);
	}

	// Cập nhật vai trò của thành viên trong nhóm
	@Transactional
	public ThanhVien capNhatVaiTro(Long nhomId, Long userId, String vaiTro) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		ThanhVien thanhVien = thanhVienRepository.findById(thanhVienId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));
		thanhVien.setVaiTro(VaiTro_ThanhVien.valueOf(vaiTro));
		return thanhVienRepository.save(thanhVien);
	}

	// Lấy danh sách thành viên của một nhóm
	public List<ThanhVien> layDanhSachThanhVien(Long nhomId) {
		return thanhVienRepository.findByNhom_Id(nhomId);
	}

//	public VaiTro_ThanhVien layVaiTroThanhVien(Long nhomId, Long userId) {
//		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
//		Optional<ThanhVien> thanhVien = thanhVienRepository.findById(thanhVienId);
//		return thanhVien.get().getVaiTro();
//	}

}