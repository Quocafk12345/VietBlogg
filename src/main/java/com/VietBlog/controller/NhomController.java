package com.VietBlog.controller;

import com.VietBlog.entity.*;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.BlockUserNhomRepository;
import com.VietBlog.repository.ThanhVienRepository;
import com.VietBlog.service.NhomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import com.VietBlog.exception.NhomNotFoundException;
import com.VietBlog.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private BlockUserNhomRepository blockUserNhomRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Nhom>> layDanhSachNhomCuaUser(@PathVariable Long userId) {
        List<Nhom> DSnhom = nhomService.layDanhSachNhomCuaThanhVien(userId);
        return ResponseEntity.ok(DSnhom);
    }

    // API tạo nhóm mới của người dùng

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
//    @GetMapping("/danh-sach")
//    public ResponseEntity<List<Nhom>> layDanhSachNhom() {
//        List<Nhom> DSnhom = nhomService.layToanBoNhom();
//        return ResponseEntity.ok(DSnhom);
//    }
    //lấy danh sách nhóm trả về thêm thông tin về vai trò của người dùng hiện tại trong mỗi nhóm lên cong-dong.html
    //Thay vì chỉ trả về danh sách Nhom, bạn sẽ trả về một danh sách các object, mỗi object chứa thông tin về
    // nhóm (Nhom) và vai trò của người dùng trong nhóm đó (vaiTro)
//    @GetMapping("/danh-sach")
//    public ResponseEntity<List<Map<String, Object>>> layDanhSachNhom(HttpServletRequest request) {
//        List<Nhom> nhomList = nhomService.layToanBoNhom();
//        User currentUser = (User) request.getSession().getAttribute("currentUser");
//
//        List<Map<String, Object>> responseList = nhomList.stream()
//                .map(nhom -> {
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("nhom", nhom);
//
//                    if (currentUser != null) {
//                        Optional<ThanhVien> thanhVienOptional = thanhVienRepository.findById(new ThanhVienId(nhom.getId(), currentUser.getId()));
//                        String vaiTro = thanhVienOptional.map(thanhVien -> thanhVien.getVaiTro().toString()).orElse("KHÔNG_THAM_GIA");
//                        response.put("vaiTro", vaiTro);
//                        // Kiểm tra xem người dùng có bị chặn khỏi nhóm hay không
//                        boolean biChan = blockUserNhomRepository.existsById(new BlockUserNhomId(currentUser.getId(), currentUser.getId(), nhom.getId()));
//                        response.put("biChan", biChan); // Thêm thông tin biChan vào response
//                    } else {
//                        response.put("vaiTro", "KHÔNG_THAM_GIA");
//                    }
//
//                    return response;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(responseList);
//    }
    @GetMapping("/danh-sach")
    public ResponseEntity<List<Map<String, Object>>> layDanhSachNhom(HttpServletRequest request) {
        List<Nhom> nhomList = nhomService.layToanBoNhom();
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        List<Map<String, Object>> responseList = nhomList.stream()
                .filter(nhom -> currentUser == null ||
                        !blockUserNhomRepository.existsByNhom_IdAndBlockedUser_Id(nhom.getId(), currentUser.getId())) // Lọc nhóm bị chặn
                .map(nhom -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("nhom", nhom);

                    if (currentUser != null) {
                        Optional<ThanhVien> thanhVienOptional = thanhVienRepository.findById(new ThanhVienId(nhom.getId(), currentUser.getId()));
                        String vaiTro = thanhVienOptional.map(thanhVien -> thanhVien.getVaiTro().toString()).orElse("KHÔNG_THAM_GIA");
                        response.put("vaiTro", vaiTro);
                    } else {
                        response.put("vaiTro", "KHÔNG_THAM_GIA");
                    }

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // API lấy thông tin một nhóm theo ID
//    @GetMapping("/{nhomId}")
//    public ResponseEntity<Nhom> layNhomTheoId(@PathVariable Long nhomId) {
//        Optional<Nhom> nhom = nhomService.layNhomTheoId(nhomId);
//	    return nhom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
    @GetMapping("/{nhomId}")
    public ResponseEntity<Map<String, Object>> layNhomTheoId(@PathVariable Long nhomId, HttpServletRequest request) {
        Optional<Nhom> nhomOptional = nhomService.layNhomTheoId(nhomId);
        if (nhomOptional.isPresent()) {
            Nhom nhom = nhomOptional.get();

            // Lấy thông tin người dùng từ session
            // Lấy vai trò của người dùng trong nhóm
            User currentUser = (User) request.getSession().getAttribute("currentUser");
            String vaiTro = "Thành viên"; // Mặc định là "Thành viên"

            // Kiểm tra xem người dùng hiện tại có phải là thành viên của nhóm không
            boolean daThamGia = currentUser != null && nhom.getThanhVien().stream()
                    .anyMatch(thanhVien -> thanhVien.getUser().getId().equals(currentUser.getId()));

            // Thêm thuộc tính daThamGia vào entity Nhom
            nhom.setDaThamGia(daThamGia); // Thêm phương thức setDaThamGia() vào entity Nhom

            if (currentUser != null) {
                Optional<ThanhVien> thanhVienOptional = thanhVienRepository.findById(new ThanhVienId(nhomId, currentUser.getId())); // Sử dụng instance thanhVienRepository
                if (thanhVienOptional.isPresent()) {
                    vaiTro = thanhVienOptional.get().getVaiTro().toString();
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("nhom", nhom);
            response.put("vaiTro", vaiTro);

            // Kiểm tra xem người dùng có bị chặn khỏi nhóm hay không
            boolean biChan = currentUser != null &&
                    blockUserNhomRepository.existsByNhom_IdAndBlockedUser_Id(nhomId, currentUser.getId());
            response.put("biChan", biChan); // Thêm thông tin biChan vào response

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
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

    // API rời khỏi nhóm và nhượng quyền trong chi-tiet-nhom.html
    @PostMapping("/{nhomId}/roi-nhom/{userId}/nhuong-quyen/{nguoiNhanId}")
    public ResponseEntity<Void> roiKhoiNhomVaNhuongQuyen(
            @PathVariable Long nhomId,
            @PathVariable Long userId,
            @PathVariable Long nguoiNhanId) {

        nhomService.roiKhoiNhomVaNhuongQuyen(nhomId, userId, nguoiNhanId);
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

    //API lấy danh sách các thành viên trong nhóm
    @GetMapping("/{nhomId}/thanh-vien")
    public ResponseEntity<List<ThanhVienDTO>> layDanhSachThanhVien(@PathVariable Long nhomId) {
        List<ThanhVien> danhSachThanhVien = thanhVienRepository.findByNhom_Id(nhomId);

        List<ThanhVienDTO> danhSachThanhVienDTO = danhSachThanhVien.stream()
                .map(thanhVien -> new ThanhVienDTO(
                        thanhVien.getUser().getTenNguoiDung(),
                        thanhVien.getVaiTro().toString(),
                        thanhVien.getUser().getHinhDaiDien(),
                        thanhVien.getUser().getId() // Set userId
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(danhSachThanhVienDTO);
    }

    // API lấy danh sách bài viết của một nhóm
//    @GetMapping("/{nhomId}/baiviet")
//    public ResponseEntity<List<BaiViet>> layDanhSachBaiVietCuaNhom(@PathVariable Long nhomId) {
//        List<BaiViet> DSBaiViet = nhomService.layDanhSachBaiVietCuaNhom(nhomId);
//        return ResponseEntity.ok(DSBaiViet);
//    }
    @GetMapping("/{nhomId}/bai-viet")
    public ResponseEntity<List<BaiViet>> layDanhSachBaiVietCuaNhom(@PathVariable Long nhomId) {
        List<BaiViet> baiVietList = baiVietRepository.findByNhom_Id(nhomId); // Sử dụng BaiVietRepository để lấy danh sách bài viết theo nhóm
        return ResponseEntity.ok(baiVietList);
    }

    // API đếm số lượng thành viên của một nhóm
    @GetMapping("/{nhomId}/thanh-vien/so-luong")
    public ResponseEntity<Integer> demSoLuongThanhVien(@PathVariable Long nhomId) {
        int soLuong = nhomService.demSoLuongThanhVien(nhomId);
        return ResponseEntity.ok(soLuong);
    }

    // API xóa thành viên khỏi nhóm
    @DeleteMapping("/{nhomId}/thanh-vien/{userId}")
    public ResponseEntity<Void> xoaThanhVienKhoiNhom(
            @PathVariable Long nhomId,
            @PathVariable Long userId) {

        nhomService.xoaThanhVienKhoiNhom(nhomId, userId);
        return ResponseEntity.ok().build();
    }




}