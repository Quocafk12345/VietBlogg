package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bai-viet")
public class BaiVietController {
    BaiVietRepository baiVietRepository;
    BinhLuanRepository binhLuanRepository;
    LuotLike_BaiViet_Repository luotLikeRepository;
    LuuBaiVietRepository luuBaiVietRepository;
    UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<List<BaiViet>> findAll() {
        return ResponseEntity.ok(baiVietRepository.findAll());
    }

    @GetMapping("/tim-kiem")
    public ResponseEntity<List<BaiViet>> findByKeyword(@RequestParam String keyword) {
        List<BaiViet> list = baiVietRepository.findByKeyword(keyword);
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<BaiViet> save(@RequestBody BaiViet baiViet) {
        baiVietRepository.save(baiViet);
        return ResponseEntity.ok(baiViet);
    }

    @PutMapping("{id}")
    public ResponseEntity<BaiViet> update(@PathVariable Integer id, @RequestBody BaiViet baiViet) {
        if (!baiVietRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        baiViet.setTrangThai("Đã chỉnh sửa");
        baiVietRepository.save(baiViet);
        return ResponseEntity.ok(baiViet);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaiViet> delete(@PathVariable Integer id) {
        if (!baiVietRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        baiVietRepository.findById(id).get().setTrangThai("Đã xóa");
        return ResponseEntity.ok().build();
    }

}
