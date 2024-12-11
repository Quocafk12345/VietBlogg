package com.VietBlog.service;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.*;
import com.VietBlog.exception.BlockedUserException;
import com.VietBlog.exception.NhomNotFoundException;
import com.VietBlog.exception.UserNotFoundException;
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
	private final DS_LuatNhom_Repository dsLuatNhomRepository;
	private final BaiVietRepository baiVietRepository;
	private final BlockUserNhomRepository blockUserNhomRepository;

	@Autowired
	public NhomService(NhomRepository nhomRepository, ThanhVienRepository thanhVienRepository, UserRepository userRepository,
					   BlockUserRepository blockUserRepository, DS_LuatNhom_Repository dsLuatNhomRepository,
					   BaiVietRepository baiVietRepository, BlockUserNhomRepository blockUserNhomRepository) {
		this.nhomRepository = nhomRepository;
		this.thanhVienRepository = thanhVienRepository;
		this.userRepository = userRepository;
		this.dsLuatNhomRepository = dsLuatNhomRepository;
		this.baiVietRepository = baiVietRepository;
		this.blockUserNhomRepository = blockUserNhomRepository;
	}

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

		// Thêm người tạo vào nhóm với vai trò "QUAN_TRI_VIEN"
		ThanhVienId thanhVienId = new ThanhVienId(nhomMoi.getId(), chuNhomId);
		ThanhVien thanhVien = new ThanhVien(thanhVienId, nhomMoi,
				userRepository.findById(chuNhomId).orElseThrow(),
				VaiTro_ThanhVien.QUAN_TRI_VIEN, LocalDate.now(), "Đang hoạt động"); // Thêm trạng thái
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
		//Kiểm tra xem người dùng có bị chặn khỏi nhóm hay kưhông
		if (blockUserNhomRepository.existsByNhom_IdAndBlockedUser_Id(nhomId, userId)) {
			throw new BlockedUserException("Người dùng đã bị chặn khỏi nhóm này.");
		}
		ThanhVien thanhVien = new ThanhVien(thanhVienId,
				nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				VaiTro_ThanhVien.THANH_VIEN, LocalDate.now(), "Đang hoạt động"); // Thêm trạng thái
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

	public Optional<Nhom> layNhomTheoId(Long nhomId) {
		return nhomRepository.findById(nhomId);
	}

	// Xóa nhóm
	@Transactional
	public void xoaNhom(Long nhomId) {
		// 1. Xóa tất cả các bản ghi trong bảng Block_User_Nhom liên quan đến nhóm
		blockUserNhomRepository.deleteAllByNhom_Id(nhomId);

		// 2. Xóa tất cả bài viết thuộc nhóm
		baiVietRepository.deleteAllByNhom_Id(nhomId);

		// 3. Xóa tất cả luật nhóm
		dsLuatNhomRepository.deleteAllByNhom_Id(nhomId);

		// 4. Xóa tất cả thành viên trong nhóm
		thanhVienRepository.deleteAllByNhom_Id(nhomId);

		// 5. Xóa nhóm
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

	//Rời nhóm và nhượng quyền cho vai trò thành viên trong nhóm
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

	public List<Nhom> layToanBoNhom() {
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

	//Xóa thành viên trong nhóm
	public void xoaThanhVienKhoiNhom(Long nhomId, Long userId) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		ThanhVien thanhVien = thanhVienRepository.findById(thanhVienId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

		// Kiểm tra vai trò
		if (thanhVien.getVaiTro() == VaiTro_ThanhVien.THANH_VIEN) {
			thanhVienRepository.deleteById(thanhVienId);
		} else {
			throw new RuntimeException("Không thể xóa quản trị viên hoặc chủ nhóm."); // Hoặc xử lý theo logic của bạn
		}
	}

	public boolean laQuanTriVien(Long nhomId, Long userId) {
		System.out.println("Kiểm tra vai trò quản trị cho người dùng " + userId + " trong nhóm " + nhomId); // In log

		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		Optional<ThanhVien> thanhVienOptional = thanhVienRepository.findById(thanhVienId);

		if (thanhVienOptional.isEmpty()) {
			System.out.println("Người dùng không phải là thành viên của nhóm"); // In log
			return false; // Người dùng không phải là thành viên của nhóm
		}

		VaiTro_ThanhVien vaiTro = thanhVienOptional.get().getVaiTro();
		System.out.println("Vai trò của người dùng: " + vaiTro); // In log

		return vaiTro == VaiTro_ThanhVien.QUAN_TRI_VIEN || vaiTro == VaiTro_ThanhVien.CHU_NHOM;
	}
}