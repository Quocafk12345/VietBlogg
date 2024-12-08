package com.VietBlog.controller;

import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienDTO;
import com.VietBlog.service.NhomService;
import com.VietBlog.service.ThanhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/nhom/thanh-vien")
public class ThanhVienController {

	private final ThanhVienService thanhVienService;
	private final NhomService nhomService;

	@Autowired
	public ThanhVienController(ThanhVienService thanhVienService, NhomService nhomService) {
		this.thanhVienService = thanhVienService;
		this.nhomService = nhomService;
	}

	// API thêm thành viên vào nhóm
	@PostMapping("/{nhomId}/them/{userId}")
	public ResponseEntity<ThanhVien> themThanhVienVaoNhom(
			@PathVariable Long nhomId,
			@PathVariable Long userId,
			@RequestParam(defaultValue = "THANH_VIEN") String vaiTro) {
		try {
			ThanhVien thanhVienMoi = thanhVienService.themThanhVien(nhomId, userId, vaiTro);
			return ResponseEntity.status(HttpStatus.CREATED).body(thanhVienMoi);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	// API xóa thành viên khỏi nhóm
	@DeleteMapping("/{nhomId}/roi-nhom/{userId}")
	public ResponseEntity<Void> xoaThanhVienKhoiNhom(@PathVariable Long nhomId, @PathVariable Long userId) {
		try {
			thanhVienService.xoaThanhVien(nhomId, userId);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	// API cập nhật vai trò của thành viên
	@PutMapping("/{nhomId}/cap-nhat/{userId}")
	public ResponseEntity<ThanhVien> capNhatVaiTroThanhVien(
			@PathVariable Long nhomId,
			@PathVariable Long userId,
			@RequestParam String vaiTro) {
		try {
			ThanhVien thanhVien = thanhVienService.capNhatVaiTro(nhomId, userId, vaiTro);
			return ResponseEntity.ok(thanhVien);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	// API đếm số lượng thành viên của một nhóm
	@GetMapping("/{nhomId}/so-luong")
	public ResponseEntity<Integer> demSoLuongThanhVien(@PathVariable Long nhomId) {
		int soLuong = nhomService.demSoLuongThanhVien(nhomId);
		return ResponseEntity.ok(soLuong);
	}

	//API lấy danh sách các thành viên trong nhóm
	@GetMapping("/{nhomId}")
	public ResponseEntity<List<ThanhVienDTO>> layDanhSachThanhVien(@PathVariable Long nhomId) {
		List<ThanhVien> danhSachThanhVien = thanhVienService.layDanhSachThanhVien(nhomId);

		List<ThanhVienDTO> danhSachThanhVienDTO = danhSachThanhVien.stream()
				.map(thanhVien -> new ThanhVienDTO(
						thanhVien.getUser().getTenNguoiDung(),
						thanhVien.getVaiTro().toString(),
						thanhVien.getUser().getHinhDaiDien(),
						thanhVien.getUser().getId() // Set userId
				))
				.collect(Collectors.toList());

		return ResponseEntity.ok(danhSachThanhVienDTO);
	}

	// API tham gia nhóm
	@PostMapping("/{nhomId}/tham-gia/{userId}")
	public ResponseEntity<Void> thamGiaNhom(@PathVariable Long nhomId, @PathVariable Long userId) {
		nhomService.thamGiaNhom(nhomId, userId); // Vai trò mặc định là "THANH_VIEN"
		return ResponseEntity.ok().build();
	}

	// API rời khỏi nhóm và nhượng quyền trong chi-tiet-nhom.html
	@PostMapping("/{nhomId}/roi-nhom/{userId}/nhuong-quyen/{nguoiNhanId}")
	public ResponseEntity<Void> roiKhoiNhomVaNhuongQuyen(
			@RequestParam Long nhomId,
			@RequestParam Long userId,
			@RequestParam Long nguoiNhanId) {

		nhomService.roiKhoiNhomVaNhuongQuyen(nhomId, userId, nguoiNhanId);
		return ResponseEntity.ok().build();
	}

}
