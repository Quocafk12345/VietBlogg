package com.VietBlog.controller;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.BinhLuanRepository;
import com.VietBlog.repository.UserRepository;
import com.VietBlog.service.BinhLuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/binh-luan/{idBaiViet}")
public class BinhLuanController {

    private final BinhLuanRepository binhLuanRepository;
    private final BaiVietRepository baiVietRepository;
    private final UserRepository userRepository;
    private final BinhLuanService binhLuanService;

    @Autowired
    public BinhLuanController(BinhLuanRepository binhLuanRepository, BaiVietRepository baiVietRepository, UserRepository userRepository, BinhLuanService binhLuanService) {
        this.binhLuanRepository = binhLuanRepository;
        this.baiVietRepository = baiVietRepository;
        this.userRepository = userRepository;
        this.binhLuanService = binhLuanService;
    }

    @GetMapping()
    public ResponseEntity<List<BinhLuan>> hienBinhLuanTheoBaiViet(@PathVariable Long idBaiViet) {
        List<BinhLuan> listBL = binhLuanRepository.findRootCommentsByBaiVietId(idBaiViet);
        return ResponseEntity.ok(listBL);
    }

    @PostMapping
    public ResponseEntity<BinhLuan> themBinhLuan(@PathVariable Long idBaiViet, @RequestBody BinhLuan binhLuan) {
        try {
            // Gán bài viết cho bình luận
            binhLuan.setBaiViet(baiVietRepository.findById(idBaiViet)
                    .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại")));

            BinhLuan binhLuanMoi = binhLuanService.themBinhLuan(binhLuan);
            return ResponseEntity.ok(binhLuanMoi);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BinhLuan()); // Trả về bad request nếu level không hợp lệ
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{idBinhLuan}/xoa")
    public ResponseEntity<Void> xoaBinhLuan(@PathVariable Long idBinhLuan) {
        try {
            binhLuanService.xoaBinhLuan(idBinhLuan);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idBinhLuan}/sua")
    public ResponseEntity<BinhLuan> capNhatBinhLuan(@PathVariable Long idBinhLuan, @RequestBody BinhLuan binhLuan) {
        try {
            BinhLuan binhLuanCapNhat = binhLuanService.capNhatBinhLuan(idBinhLuan, binhLuan);
            return ResponseEntity.ok(binhLuanCapNhat);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
