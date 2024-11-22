package com.VietBlog.service;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import com.VietBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	private BaiVietRepository baiVietRepository; // Khai báo BaiVietRepository

	@Autowired
	private DS_LuatNhom_Repository dsLuatNhomRepository;
	// Tạo nhóm mới
//	@Transactional
//	public Nhom taoNhom(Nhom nhom, Long chuNhomId) {
//		nhom.setNgayTao(Timestamp.from(Instant.now()));
//		Nhom nhomMoi = nhomRepository.save(nhom);
//
//		// Thêm người tạo vào nhóm với vai trò "CHỦ NHÓM"
//		ThanhVienId thanhVienId = new ThanhVienId(nhomMoi.getId(), chuNhomId);
//		ThanhVien thanhVien = new ThanhVien(thanhVienId, nhomMoi,
//				userRepository.findById(chuNhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
//				VaiTro_ThanhVien.CHU_NHOM, LocalDate.now());
//		thanhVienRepository.save(thanhVien);
//
//		return nhomMoi;
//	}

	@Transactional
	public Nhom taoNhom(Nhom nhom, Long chuNhomId) {
		nhom.setNgayTao(Timestamp.from(Instant.now()));
		// Xử lý lưu hình ảnh (nếu có)
		Nhom nhomMoi = nhomRepository.save(nhom);

		// Thêm người tạo vào nhóm với vai trò "CHỦ NHÓM"
		ThanhVienId thanhVienId = new ThanhVienId(nhomMoi.getId(), chuNhomId);
		ThanhVien thanhVien = new ThanhVien(thanhVienId, nhomMoi,
				userRepository.findById(chuNhomId).orElseThrow(),
				VaiTro_ThanhVien.CHU_NHOM, LocalDate.now());
		thanhVienRepository.save(thanhVien);

		return nhomMoi;
	}

	// Tham gia nhóm
	@Transactional
	public ThanhVien thamGiaNhom(Long nhomId, Long userId) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng đã tham gia nhóm này rồi");
		}
		ThanhVien thanhVien = new ThanhVien(thanhVienId,
				nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());
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

	public Optional<Nhom> layNhomTheoId(Long nhomId){
		return nhomRepository.findById(nhomId);
	}

	// Xóa nhóm
	@Transactional
	public void xoaNhom(Long nhomId) {
		// 1. Xóa tất cả bài viết thuộc nhóm
		baiVietRepository.deleteAllByNhom_Id(nhomId);

		// 2. Xóa tất cả luật nhóm
		dsLuatNhomRepository.deleteAllByNhom_Id(nhomId); // Thêm dòng này

		// 3. Xóa tất cả thành viên trong nhóm
		thanhVienRepository.deleteAllByNhom_Id(nhomId);

		// 4. Xóa nhóm
		nhomRepository.deleteById(nhomId);
	}

	public Nhom capNhatNhom(Long nhomId, Nhom capNhat) {
		Optional<Nhom> nhom = nhomRepository.findById(nhomId);
		if (nhom.isPresent()) {
			Nhom hienTai = nhom.get();
			hienTai.setTen(capNhat.getTen());
			hienTai.setGioiThieu(capNhat.getGioiThieu());
			return nhomRepository.save(hienTai);
		} else {
			throw new RuntimeException("Bài viết không tồn tại");
		}
	}

	public void roiKhoiNhomVaNhuongQuyen(Long nhomId, Long userId, Long nguoiNhanId) {
		// 1. Kiểm tra vai trò của người dùng hiện tại
		ThanhVien thanhVienHienTai = thanhVienRepository.findById(new ThanhVienId(nhomId, userId))
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

		if (thanhVienHienTai.getVaiTro() == VaiTro_ThanhVien.CHU_NHOM ||
				thanhVienHienTai.getVaiTro() == VaiTro_ThanhVien.QUAN_TRI_VIEN) {

			// 2. Cập nhật vai trò của người được nhượng quyền
			ThanhVien thanhVienNhanQuyen = thanhVienRepository.findById(new ThanhVienId(nhomId, nguoiNhanId))
					.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên được nhượng quyền"));
			thanhVienNhanQuyen.setVaiTro(thanhVienHienTai.getVaiTro()); // Nhượng quyền
			thanhVienRepository.save(thanhVienNhanQuyen);
		}

		// 3. Rời khỏi nhóm
		roiKhoiNhom(nhomId, userId);
	}

	// Lấy danh sách nhóm theo người tạo
	public List<Nhom> layDanhSachNhomTheoNguoiTao(Long userId) {
		return nhomRepository.findNhomByNguoiTao(userId);
	}

	// Lấy danh sách bài viết của một nhóm
	public List<BaiViet> layDanhSachBaiVietCuaNhom(Long nhomId) {
		return nhomRepository.findBaiVietByNhomId(nhomId);
	}

	public List<Nhom> layToanBoNhom(){
		return nhomRepository.findAll();
	}

	// Đếm số lượng thành viên của một nhóm
	public int demSoLuongThanhVien(Long nhomId) {
		return nhomRepository.countThanhVienByNhomId(nhomId);
	}

	public List<Nhom> layDanhSachNhomDaThamGia(Long userId) {
		return nhomRepository.findNhomByUserId(userId);
	}

	// Lấy danh sách nhóm của một người dùng
	public List<Nhom> layDanhSachNhomCuaThanhVien(Long userId) {
		return nhomRepository.findNhomByUserId(userId);
	}

}