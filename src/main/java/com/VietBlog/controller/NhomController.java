package com.VietBlog.controller;

import com.VietBlog.entity.Nhom;
import com.VietBlog.service.NhomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/nhom")
public class NhomController {
    private final NhomService nhomService;

    @Autowired
    public NhomController(NhomService nhomService) {
        this.nhomService = nhomService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomCuaThanhVien(@PathVariable Long userId) {
        List<Nhom> nhoms = nhomService.layDanhSachNhomCuaThanhVien(userId);
        return ResponseEntity.ok(nhoms);
    }

}