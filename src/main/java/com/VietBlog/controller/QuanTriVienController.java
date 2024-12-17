package com.VietBlog.controller;

import com.VietBlog.service.BaiVietService; // Import BaiVietService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quan-tri-vien")
public class QuanTriVienController {

    @Autowired
    private BaiVietService baiVietService; // Inject BaiVietService

    @GetMapping("/posts/count-by-month-all-users")
    public ResponseEntity<List<Map<String, Object>>> countPostsByMonthAllUsers() {
        List<Map<String, Object>> counts = baiVietService.countPostsByMonthAllUsers();
        return ResponseEntity.ok(counts);
    }
}