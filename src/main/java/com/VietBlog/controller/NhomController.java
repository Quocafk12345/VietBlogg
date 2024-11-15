package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.Nhom;
import com.VietBlog.service.NhomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/nhom")
public class NhomController {
    private final NhomService nhomService;
    @Autowired
    private Cloudinary cloudinary; // Khai báo bean Cloudinary

    @Autowired
    public NhomController(NhomService nhomService) {
        this.nhomService = nhomService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomCuaUser(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomCuaThanhVien(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API tạo nhóm mới
//    @PostMapping("/tao-nhom")
//    public ResponseEntity<Nhom> taoNhom(@RequestBody Nhom nhom, @RequestParam Long userId) {
//        Nhom nhomMoi = nhomService.taoNhom(nhom, userId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(nhomMoi);
//    }

    @PostMapping("/tao-nhom")
    public ResponseEntity<Nhom> taoNhom(
            @RequestParam("userId") Long userId,
            @RequestParam("ten") String ten,
            @RequestParam("gioiThieu") String gioiThieu,
            @RequestParam("hinhAnh") MultipartFile hinhAnh
    ) {
        Nhom nhom = new Nhom();
        nhom.setTen(ten);
        nhom.setGioiThieu(gioiThieu);

        // Xử lý hình ảnh (ví dụ: sử dụng Cloudinary)
        if (!hinhAnh.isEmpty()) {
            try {
                // Lưu hình ảnh lên Cloudinary
                Map uploadResult = cloudinary.uploader().upload(hinhAnh.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                nhom.setHinhDaiDien(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Nhom nhomMoi = nhomService.taoNhom(nhom, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nhomMoi);
    }

    // API lấy danh sách tất cả các nhóm
    @GetMapping("/danh-sach")
    public ResponseEntity<List<Nhom>> layDanhSachNhom() {
        List<Nhom> DSnhom = nhomService.layToanBoNhom();
        return ResponseEntity.ok(DSnhom);
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

    // API tham gia nhóm
    @PostMapping("/{nhomId}/tham-gia/{userId}")
    public ResponseEntity<Void> thamGiaNhom(@PathVariable Long nhomId, @PathVariable Long userId) {
        nhomService.thamGiaNhom(nhomId, userId); // Vai trò mặc định là "THANH_VIEN"
        return ResponseEntity.ok().build();
    }

    // API rời khỏi nhóm
    @PostMapping("/{nhomId}/roi-nhom/{userId}")
    public ResponseEntity<Void> roiKhoiNhom(@PathVariable Long nhomId, @PathVariable Long userId) {
        nhomService.roiKhoiNhom(nhomId, userId);
        return ResponseEntity.ok().build();
    }

    // API lấy danh sách nhóm theo người tạo
    @GetMapping("/nguoitao/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomTheoNguoiTao(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomTheoNguoiTao(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API lấy danh sách nhóm mà người dùng đã tham gia
    @GetMapping("/thamgia/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomDaThamGia(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomDaThamGia(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API lấy danh sách bài viết của một nhóm
    @GetMapping("/{nhomId}/baiviet")
    public ResponseEntity<List<BaiViet>> layDanhSachBaiVietCuaNhom(@PathVariable Long nhomId) {
        List<BaiViet> DSBaiViet = nhomService.layDanhSachBaiVietCuaNhom(nhomId);
        return ResponseEntity.ok(DSBaiViet);
    }

    // API đếm số lượng thành viên của một nhóm
    @GetMapping("/{nhomId}/thanh-vien/so-luong")
    public ResponseEntity<Integer> demSoLuongThanhVien(@PathVariable Long nhomId) {
        int soLuong = nhomService.demSoLuongThanhVien(nhomId);
        return ResponseEntity.ok(soLuong);
    }


}