package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import com.VietBlog.service.NhomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/nhom")
public class NhomController {

    private final NhomService nhomService;

    @Autowired
    public NhomController(NhomService nhomService){
	    this.nhomService = nhomService;
    }

    // lấy DS nhóm của User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomCuaUser(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomDaThamGia(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API tạo nhóm mới của người dùng
    @PostMapping(value = "/tao-nhom", consumes = "multipart/form-data")
    public ResponseEntity<Nhom> taoNhom(@RequestBody Nhom nhom, @RequestParam Long userId, @RequestParam MultipartFile anhDaiDien) {
        try {
            Nhom nhomMoi = nhomService.taoNhom(nhom, userId, anhDaiDien);
            return ResponseEntity.status(HttpStatus.CREATED).body(nhomMoi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API lấy danh sách nhóm
    @GetMapping("/danh-sach")
    public ResponseEntity<List<Nhom>> layDanhSachNhom() {
        try {
            List<Nhom> nhomList = nhomService.layToanBoNhom();
            return ResponseEntity.ok(nhomList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API lấy thông tin một nhóm theo ID
    @GetMapping("/{nhomId}")
    public ResponseEntity<Nhom> layNhomTheoId(@PathVariable Long nhomId) {
        Optional<Nhom> nhom = nhomService.layNhomTheoId(nhomId);
	    return nhom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API cập nhật thông tin giới thiêu một nhóm
    @PutMapping("/{nhomId}")
    public ResponseEntity<Nhom> capNhatNhom(@PathVariable Long nhomId, @RequestBody Nhom nhomSauCapNhat) {
        nhomSauCapNhat.setId(nhomId); // Đảm bảo ID không bị thay đổi
        Nhom nhomCapNhat = nhomService.capNhatNhom(nhomId, nhomSauCapNhat); // Cần implement phương thức này trong NhomService
        return ResponseEntity.ok(nhomCapNhat);
    }

    // API xóa một nhóm
    @DeleteMapping("/{nhomId}")
    public ResponseEntity<Void> xoaNhom(@PathVariable Long nhomId) {
        nhomService.xoaNhom(nhomId); // Cần implement phương thức này trong NhomService
        return ResponseEntity.noContent().build();
    }

    // API lấy danh sách nhóm theo người tạo
    @GetMapping("/nguoi-tao/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomTheoNguoiTao(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomTheoNguoiTao(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API lấy danh sách nhóm mà người dùng đã tham gia
    @GetMapping("/tham-gia/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomDaThamGia(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomDaThamGia(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API lấy danh sách bài viết của một nhóm
    @GetMapping("/{nhomId}/bai-viet")
    public ResponseEntity<List<BaiViet>> layDanhSachBaiVietCuaNhom(@PathVariable Long nhomId) {
	    try {
		    List<BaiViet> DSBaiViet = nhomService.layDSBaiVietCuaNhom(nhomId);
		    return ResponseEntity.ok(DSBaiViet);
	    } catch (Exception e) {
		    e.printStackTrace();
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
    }

}