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
	                                                             @RequestParam("DSMoTa") String[] DSMoTa) {
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

	@PutMapping("/chinh-sua")
	public ResponseEntity<DaPhuongTien> chinhSuaDaPhuongTien(@RequestParam("daPhuongTien") DaPhuongTien daPhuongTien, @RequestParam("file") MultipartFile file) {
		try {
			return ResponseEntity.ok(daPhuongTienService.capNhatDaPhuongTien(daPhuongTien, file));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/xoa")
	public ResponseEntity<?> xoaDaPhuongTien(@RequestParam("daPhuongTien") DaPhuongTien daPhuongTien) {
		try {
			daPhuongTienService.xoaDaPhuongTien(daPhuongTien);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public List<DaPhuongTienDTO> chuyenDoi(List<DaPhuongTien> listDaPhuongTien) {
		return listDaPhuongTien.stream()
				.map(daPhuongTien -> new DaPhuongTienDTO(
						daPhuongTien.getDuongDan(),
						daPhuongTien.getMoTa()
				))
				.collect(Collectors.toList());
	}
}
