package com.VietBlog.service;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import com.VietBlog.repository.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NhomService {

	private final NhomRepository nhomRepository;
	private final ThanhVienRepository thanhVienRepository;
	private final UserRepository userRepository;
	private final Cloudinary cloudinary;
	private final DS_LuatNhom_Repository dsLuatNhomRepository;
	private final BaiVietRepository baiVietRepository;

	@Autowired
	public NhomService(NhomRepository nhomRepository, ThanhVienRepository thanhVienRepository, UserRepository userRepository, Cloudinary cloudinary, DS_LuatNhom_Repository dsLuatNhomRepository, BaiVietRepository baiVietRepository) {
		this.nhomRepository = nhomRepository;
		this.thanhVienRepository = thanhVienRepository;
		this.userRepository = userRepository;
		this.cloudinary = cloudinary;
		this.dsLuatNhomRepository = dsLuatNhomRepository;
		this.baiVietRepository = baiVietRepository;
	}

	// tạo nhóm mới
	@Transactional
	public Nhom taoNhom(Nhom nhom, Long chuNhomId, MultipartFile anhDaiDien) throws IOException {
		nhom.setNgayTao(Timestamp.from(Instant.now()));
		if (!anhDaiDien.isEmpty()){
			Map uploadResult = cloudinary.uploader().upload(anhDaiDien.getBytes(), ObjectUtils.emptyMap());
			String imageUrl = (String) uploadResult.get("secure_url");
			nhom.setHinhDaiDien(imageUrl);
		}
		Nhom nhomMoi = nhomRepository.save(nhom);

		// Thêm người tạo vào nhóm với vai trò "CHỦ NHÓM"
		ThanhVienId thanhVienId = new ThanhVienId(nhomMoi.getId(), chuNhomId);
		ThanhVien thanhVien = new ThanhVien(thanhVienId, nhomMoi,
				userRepository.findById(chuNhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				VaiTro_ThanhVien.QUAN_TRI_VIEN, LocalDate.now());
		thanhVienRepository.save(thanhVien);

		return nhomMoi;
	}

	// Tham gia nhóm
	@Transactional
	public void thamGiaNhom(Long nhomId, Long userId) {
		ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);
		if (thanhVienRepository.existsById(thanhVienId)) {
			throw new RuntimeException("Người dùng đã tham gia nhóm này rồi");
		}
		ThanhVien thanhVien = new ThanhVien(thanhVienId,
				nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
				VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());
		thanhVienRepository.save(thanhVien);
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

	//Rời nhóm và nhượng quyền cho vai trò thành viên trong nhóm
	@Transactional
	public void roiKhoiNhomVaNhuongQuyen(Long nhomId, Long Id_QTVHienTai, Long Id_QTVMoi) {
		// 1. Kiểm tra vai trò của người dùng hiện tại
		ThanhVien QTVHienTai = thanhVienRepository.findById(new ThanhVienId(nhomId, Id_QTVHienTai))
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

		if (QTVHienTai.getVaiTro() == VaiTro_ThanhVien.QUAN_TRI_VIEN) {
			// 2. Cập nhật vai trò của người được nhượng quyền
			ThanhVien thanhVienNhuongQuyen = thanhVienRepository.findById(new ThanhVienId(nhomId, Id_QTVMoi))
					.orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên được nhượng quyền"));
			thanhVienNhuongQuyen.setVaiTro(QTVHienTai.getVaiTro()); // Nhượng quyền
			thanhVienRepository.save(thanhVienNhuongQuyen);
		}

		// 3. Rời khỏi nhóm
		thanhVienRepository.delete(QTVHienTai);
	}

	// Lấy danh sách nhóm theo người tạo
	public List<Nhom> layDanhSachNhomTheoNguoiTao(Long userId) {
		return nhomRepository.findNhomByNguoiTao(userId);
	}

	// Lấy danh sách bài viết của một nhóm
	public List<BaiViet> layDSBaiVietCuaNhom(Long nhomId) {
		return nhomRepository.findBaiVietByNhomId(nhomId);
	}

	public List<BaiViet> layBaiVietCuaUser(Long nhomId, Long userId) {
		return baiVietRepository.findByNhomIdAndUserId(nhomId, userId);
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

}