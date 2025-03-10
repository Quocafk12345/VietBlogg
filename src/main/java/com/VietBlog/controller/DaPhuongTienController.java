package com.VietBlog.controller;

import com.VietBlog.entity.DaPhuongTien;
import com.VietBlog.entity.DaPhuongTienDTO;
import com.VietBlog.service.DaPhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bai-viet/da-phuong-tien")
public class DaPhuongTienController {

	private final DaPhuongTienService daPhuongTienService;

	@Autowired
	public DaPhuongTienController(DaPhuongTienService daPhuongTienService) {
		this.daPhuongTienService = daPhuongTienService;
	}

	@GetMapping("/{idBaiViet}")
	public ResponseEntity<List<DaPhuongTienDTO>> layDSDaPhuongTienChoBaiViet(@PathVariable Long idBaiViet) {
		try {
			List<DaPhuongTien> DSDaPhuongTien = daPhuongTienService.layDaPhuongTienTheoBaiViet(idBaiViet);
			return ResponseEntity.ok(chuyenDoi(DSDaPhuongTien));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = "/dang-tai/{baiVietId}", consumes = "multipart/form-data")
	public ResponseEntity<List<DaPhuongTien>> uploadDaPhuongTien(@PathVariable("baiVietId") Long baiVietId,
	                                                             @RequestParam("DSfile") MultipartFile[] DSfile,
	                                                             @RequestParam(value = "DSMoTa", required = false) String[] DSMoTa) {
		try {
			List<DaPhuongTien> daPhuongTiens = new ArrayList<>();

			for (int i = 0; i < DSfile.length; i++) {
				MultipartFile file = DSfile[i];
				String moTa = DSMoTa[i];
				DaPhuongTien daPhuongTien = daPhuongTienService.themDaPhuongTienChoBaiViet(baiVietId, file, moTa);
				daPhuongTiens.add(daPhuongTien);
			}
			return ResponseEntity.ok(daPhuongTiens);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping(value = "/chinh-sua/{id}", consumes = "multipart/form-data")
	public ResponseEntity<DaPhuongTien> chinhSuaDaPhuongTien(@PathVariable Long id, @RequestParam("moTa") String moTa) {
		try {
			return ResponseEntity.ok(daPhuongTienService.capNhatDaPhuongTien(id, moTa));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/xoa/{id}")
	public ResponseEntity<?> xoaDaPhuongTien(@PathVariable("id") Long id) {
		try {
			daPhuongTienService.xoaDaPhuongTien(daPhuongTienService.layDaPhuongTienTheoId(id));
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public List<DaPhuongTienDTO> chuyenDoi(List<DaPhuongTien> listDaPhuongTien) {
		return listDaPhuongTien.stream()
				.map(daPhuongTien -> new DaPhuongTienDTO(
						daPhuongTien.getId(),
						daPhuongTien.getDuongDan(),
						daPhuongTien.getMoTa(),
						daPhuongTien.getLoai()
				))
				.collect(Collectors.toList());
	}
}
