package com.VietBlog.controller;

import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.User;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.BinhLuanService;
import com.VietBlog.service.LuotLike_BaiViet_Service;
import com.VietBlog.service.LuuBaiVietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bai-viet")
public class BaiVietController {

    private final BaiVietService baiVietService;
    private final LuuBaiVietService luuBaiVietService;
    private final LuotLike_BaiViet_Service luotLike_BaiViet_Service;
    private final BinhLuanService binhLuanService;

    @Autowired
    public BaiVietController(BaiVietService baiVietService, LuotLike_BaiViet_Service luotLike_BaiViet_Service, LuuBaiVietService luuBaiVietService, BinhLuanService binhLuanService) {
        this.baiVietService = baiVietService;
        this.luotLike_BaiViet_Service = luotLike_BaiViet_Service;
        this.luuBaiVietService = luuBaiVietService;
        this.binhLuanService = binhLuanService;
    }

    /**
     * Phương thức lấy tất cả bài đăng
     *
     */
    @Operation(summary = "Lấy tất cả bài viết", description = "Lấy thông tin chi tiết của tất cả bài viết.")
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = BaiViet.class)))
    @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết")
    @GetMapping
    public ResponseEntity<List<BaiViet>> findAll(){
        return ResponseEntity.ok(baiVietService.getAllBaiViet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaiViet> findById(@PathVariable Long id){
        return ResponseEntity.ok(baiVietService.getBaiVietById(id));
    }

    @GetMapping("/nhom/{nhomId}/user/{userId}")
    public ResponseEntity<List<BaiViet>> layDanhSachBaiVietCuaUserTrongNhom(
            @PathVariable Long nhomId,
            @PathVariable Long userId) {

        List<BaiViet> baiVietList = baiVietService.getBaiVietByNhomIdAndUserId(nhomId, userId);
        return ResponseEntity.ok(baiVietList);
    }
    @Operation(summary = "Lấy tất cả bài viết của người dùng", description = "Lấy thông tin chi tiết của tất cả bài viết của người dùng theo userId.")
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = BaiViet.class)))
    @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết của người dùng")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BaiViet>> findByUserId(@PathVariable Long userId){
        List<BaiViet> baiViet = baiVietService.getBaiVietByUserId(userId);
        if (baiViet.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(baiViet);
        }
    }


    /**
     * Phương thức đếm số lượt like của một bài viết
     * @param id: Id của bài viết
     *
     */
    @Operation(summary = "Đếm số lượt like", description = "Đếm số lượt like của một bài viết")
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = BaiViet.class)))
    @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết")
    @GetMapping("/{id}/luot-like")
    public ResponseEntity<Integer> demLuotThich(@PathVariable Long id) {
        Integer luotLike = luotLike_BaiViet_Service.demLuotLikeTheoBaiViet(id);
        return ResponseEntity.ok(luotLike);
    }

    /**
     * Phương thức đếm số lượt bình luận của một bài viết
     * @param id: Id của bài viết
     *
     */
    @GetMapping("/{id}/luot-binh-luan")
    public ResponseEntity<Integer> demBinhLuan(@PathVariable Long id) {
        Integer luotBL = binhLuanService.demLuotBinhLuan(id);
        return ResponseEntity.ok(luotBL);
    }

    /**
     * Phương thức tìm kiếm bài đăng bằng từ khóa (tên người dùng, nội dung, tiêu đề bài viết)
     * @param keyword: từ khóa cần tìm
     */
    @GetMapping("/tim-kiem")
    public ResponseEntity<List<BaiViet>> findByKeyword(@RequestParam String keyword) {
        List<BaiViet> list = baiVietService.getBaiVietByKeyword(keyword); // Sử dụng BaiVietService
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(list);
    }

    /**
     * Phương thức đăng bài
     * @param baiViet: Các thông tin của một bài viết
     */
    @PostMapping("/dang-bai")
    @Transactional
    public ResponseEntity<BaiViet> dangBaiViet(@RequestBody BaiViet baiViet) {
        try {
             // Sử dụng BaiVietService
            baiViet.setTrangThai(TrangThai_BaiViet.DA_DANG);
            baiVietService.themBaiViet(baiViet);
            return ResponseEntity.ok(baiViet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Hoặc trả về thông báo lỗi cụ thể hơn
        }
    }

    /**
     * Phương thức lưu bài về Xem sau
     * @param baiViet: Bài viết được chọn
     * @param user: User đang đăng nhập
     *
     */
    @Transactional
    @PostMapping("/luu-bai")
    public ResponseEntity<?> luuBaiVietVaoDSLuu(@RequestBody BaiViet baiViet, @RequestBody User user) {
        luuBaiVietService.luuBaiViet(user.getId(), baiViet.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Phương thức chỉnh sửa bài viết
     * @param id: id của bài viết
     * @param baiViet: nội dung mới, sẽ được cập nhật của bài viết đó
     */

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<BaiViet> update(@PathVariable Long id, @RequestBody BaiViet baiViet) {
        try {
            BaiViet baiVietCapNhat = baiVietService.capNhatBaiViet(id, baiViet); // Sử dụng BaiVietService
            return ResponseEntity.ok(baiVietCapNhat);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    /**
     * Phương thức xóa bài
     * @param id: Id của bài viết
     *
     */
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            baiVietService.xoaBaiViet(id); // Sử dụng BaiVietService
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/thich")
    public boolean toggleLike(@RequestParam("idBaiViet") Long idBaiViet, @RequestParam("userId") Long userId) {
        try {
            return luotLike_BaiViet_Service.toggleLike(idBaiViet, userId);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()).hasBody();
        }
    }

    @GetMapping("/thich/kiem-tra")
    public boolean kiemTraLike(@RequestParam("idBaiViet") Long idBaiViet, @RequestParam("userId") Long userId) {
        try {
            return luotLike_BaiViet_Service.kiemTraLikeBaiViet(userId, idBaiViet);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()).hasBody();
        }
    }
}