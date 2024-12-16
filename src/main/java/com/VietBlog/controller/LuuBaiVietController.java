package com.VietBlog.controller;

import com.VietBlog.service.LuuBaiVietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bai-viet/luu")
public class LuuBaiVietController {

	private final LuuBaiVietService luuBaiVietService;

	@Autowired
	public LuuBaiVietController(LuuBaiVietService luuBaiVietService) {
		this.luuBaiVietService = luuBaiVietService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> layDSLuu(@PathVariable Long userId){
		try {
			return ResponseEntity.ok(luuBaiVietService.layDanhSachBaiVietDaLuu(userId));
		} catch ( Exception e ) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> luuBaiViet (@RequestParam Long baiVietId, @RequestParam Long userId) {
		try {
			return ResponseEntity.ok(luuBaiVietService.luuBaiViet(userId, baiVietId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@DeleteMapping("/xoa")
	public ResponseEntity<?> boLuuBaiViet(@RequestParam Long baiVietId, @RequestParam Long userId) {
		try {
			luuBaiVietService.boLuuBaiViet(userId, baiVietId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
