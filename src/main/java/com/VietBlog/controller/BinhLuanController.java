package com.VietBlog.controller;

import com.VietBlog.entity.BinhLuan;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.BinhLuanRepository;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.BinhLuanService;
import com.VietBlog.service.LuotLike_BinhLuan_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/binh-luan")
public class BinhLuanController {

    private final BaiVietService baiVietService;
    private final BinhLuanService binhLuanService;
    private final LuotLike_BinhLuan_Service luotLike_BinhLuan_Service;

    @Autowired
    public BinhLuanController(BaiVietService baiVietService, BinhLuanService binhLuanService, LuotLike_BinhLuan_Service luotLike_BinhLuan_Service) {
        this.baiVietService = baiVietService;
        this.binhLuanService = binhLuanService;
        this.luotLike_BinhLuan_Service = luotLike_BinhLuan_Service;
    }

    @GetMapping("/{idBaiViet}/binh-luan-goc")
    public ResponseEntity<List<BinhLuan>> hienBinhLuanTheoBaiViet(@PathVariable Long idBaiViet) {
        List<BinhLuan> listBL = binhLuanService.layBinhLuanGoc(idBaiViet);
        return ResponseEntity.ok(listBL);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BinhLuan>> hienBinhLuanTheoUserId(@PathVariable Long userId) {
        List<BinhLuan> listBL = binhLuanService.getBinhLuanByUserId(userId);
        return ResponseEntity.ok(listBL);
    }

    @GetMapping("/{binhLuanChaId}/binh-luan-con")
    public ResponseEntity<List<BinhLuan>> layBinhLuanCon(@PathVariable Long binhLuanChaId) {
        List<BinhLuan> binhLuanCon = binhLuanService.layBinhLuanCon(binhLuanChaId);
        return ResponseEntity.ok(binhLuanCon);
    }

    @PostMapping("/{idBaiViet}")
    public ResponseEntity<BinhLuan> themBinhLuan(@PathVariable Long idBaiViet, @RequestBody BinhLuan binhLuan) {
        try {
            // Gán bài viết cho bình luận
            binhLuan.setBaiViet(baiVietService.getBaiVietById(idBaiViet));
            BinhLuan binhLuanMoi = binhLuanService.themBinhLuan(binhLuan);
            return ResponseEntity.ok(binhLuanMoi);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new BinhLuan()); // Trả về bad request nếu level không hợp lệ
        } catch (Exception e) {
            e.printStackTrace();
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
