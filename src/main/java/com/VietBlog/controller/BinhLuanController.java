package com.VietBlog.controller;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.BinhLuanRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/binh-luan")
public class BinhLuanController {

    @Autowired
    private BinhLuanRepository binhLuanRepository;

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<List<BinhLuan>> hienBinhLuanTheoBaiViet(@PathVariable Long idBaiViet) {
        List<BinhLuan> listBL = binhLuanRepository.findBinhLuanByBaiViet_Id(idBaiViet);
        return ResponseEntity.ok(listBL);
    }

//    @PostMapping
//    @Transactional
//    public ResponseEntity<BinhLuan> themBinhLuan(@PathVariable Integer idBaiViet, @RequestBody BinhLuan binhLuan) {
//
//    }
}
