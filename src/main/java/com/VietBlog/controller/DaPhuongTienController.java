package com.VietBlog.controller;

import com.VietBlog.entity.DaPhuongTien;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.DaPhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/da-phuong-tien")
public class DaPhuongTienController {

	private final DaPhuongTienService daPhuongTienService;
	private final BaiVietService baiVietService;

	@Autowired
	public DaPhuongTienController(DaPhuongTienService daPhuongTienService, BaiVietService baiVietService) {
		this.daPhuongTienService = daPhuongTienService;
		this.baiVietService = baiVietService;
	}

	@GetMapping("/bai-viet/{idBaiViet}")
	public ResponseEntity<List<DaPhuongTien>> layDSDaPhuongTienChoBaiViet(@PathVariable Long idBaiViet) {
		try {
			List<DaPhuongTien> DSPhuongTien = daPhuongTienService.layDaPhuongTienTheoBaiViet(idBaiViet);
			return ResponseEntity.ok(DSPhuongTien);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/dang-tai/{baiVietId}")
	public ResponseEntity<DaPhuongTien> uploadDaPhuongTien(@RequestParam("baiVietId") Long baiVietId, @RequestParam("file") MultipartFile file) {
		try {
			return ResponseEntity.ok(daPhuongTienService.themDaPhuongTienChoBaiViet(baiVietId, file));
		} catch (Exception e) {
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

	@DeleteMapping
	public ResponseEntity<?> xoaDaPhuongTien(@RequestParam("daPhuongTien") DaPhuongTien daPhuongTien) {
		try {
			daPhuongTienService.xoaDaPhuongTien(daPhuongTien);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
