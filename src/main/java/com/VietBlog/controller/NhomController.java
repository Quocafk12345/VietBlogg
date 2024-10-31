package com.VietBlog.controller;


import com.VietBlog.entity.Nhom;
import com.VietBlog.repository.NhomRepository;
import com.VietBlog.service.NhomService;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/nhom")
public class NhomController {

	private final NhomService nhomService;

	@Autowired
	public NhomController(NhomService nhomService) {
		this.nhomService = nhomService;
	}
	@GetMapping
	public ResponseEntity<List<Nhom>> getAll() {
		return ResponseEntity.ok(nhomService.layToanBoNhom());
	}

	@PostMapping("/tao")
	public ResponseEntity<Nhom> create(@RequestBody Nhom nhom) {
		try {
			nhomService.taoNhom(nhom, 1L);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		nhomService.taoNhom(nhom, 1L);
		return ResponseEntity.ok(nhom);
	}
//	@PutMapping
//	@DeleteMapping
}
