package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import com.VietBlog.repository.NhomRepository;
import com.VietBlog.repository.ThanhVienRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class NhomService {

	private final NhomRepository nhomRepository;
	private final ThanhVienRepository thanhVienRepository;
	private final UserRepository userRepository;

	@Autowired
	public NhomService(NhomRepository nhomRepository, ThanhVienRepository thanhVienRepository, UserRepository userRepository) {
		this.nhomRepository = nhomRepository;
		this.thanhVienRepository = thanhVienRepository;
		this.userRepository = userRepository;
	}

	// Tạo nhóm mới
	@Transactional
	public Nhom taoNhom(Nhom nhom, Long userId) {
		nhom.setNgayTao(Timestamp.from(Instant.now()));
		Nhom nhomMoi = nhomRepository.save(nhom);

		// Thêm người tạo vào nhóm với vai trò "CHỦ NHÓM"
		ThanhVienId thanhVienId = new ThanhVienId(nhomMoi.getId(), userId);
		ThanhVien thanhVien = new ThanhVien(thanhVienId, nhomMoi,
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				"CHỦ NHÓM", LocalDate.now());
		thanhVienRepository.save(thanhVien);

		return nhomMoi;
	}

	// Tham gia nhóm
	@Transactional
	public ThanhVien thamGiaNhom(Long nhomId, Long userId, String vaiTro) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng đã tham gia nhóm này rồi");
		}
		ThanhVien thanhVien = new ThanhVien(thanhVienId,
				nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				vaiTro, LocalDate.now());
		return thanhVienRepository.save(thanhVien);
	}

	// Rời khỏi nhóm
	@Transactional
	public void roiKhoiNhom(Long nhomId, Long userId) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (!thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng chưa tham gia nhóm này");
		}
		thanhVienRepository.deleteById(thanhVienId);
	}

	// Lấy danh sách nhóm theo người tạo
	public List<Nhom> layDanhSachNhomTheoNguoiTao(Long userId) {
		return nhomRepository.findNhomByNguoiTao(userId);
	}

	// Lấy danh sách bài viết của một nhóm
	public List<BaiViet> layDanhSachBaiVietCuaNhom(Long nhomId) {
		return nhomRepository.findBaiVietByNhomId(nhomId);
	}

	// Đếm số lượng thành viên của một nhóm
	public int demSoLuongThanhVien(Long nhomId) {
		return nhomRepository.countThanhVienByNhomId(nhomId);
	}

}