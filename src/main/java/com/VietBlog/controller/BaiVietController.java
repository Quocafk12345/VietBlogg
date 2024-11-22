package com.VietBlog.controller;

import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import com.VietBlog.entity.User;
import com.VietBlog.repository.*;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.LuotLike_BaiViet_Service;
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
    private final BinhLuanRepository binhLuanRepository;
    private final LuotLike_BaiViet_Repository luotLikeRepository;
    private final LuuBaiVietRepository luuBaiVietRepository;
    private final LuotLike_BaiViet_Service luotLike_BaiViet_Service;

    @Autowired
    public BaiVietController(BaiVietService baiVietService, BinhLuanRepository binhLuanRepository,
                             LuotLike_BaiViet_Repository luotLikeRepository, LuuBaiVietRepository luuBaiVietRepository, LuotLike_BaiViet_Service luotLike_BaiViet_Service) {
        this.baiVietService = baiVietService;
        this.binhLuanRepository = binhLuanRepository;
        this.luotLikeRepository = luotLikeRepository;
        this.luuBaiVietRepository = luuBaiVietRepository;
        this.luotLike_BaiViet_Service = luotLike_BaiViet_Service;
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
        Integer luotLike = luotLikeRepository.countLuotLike_BaiVietByBaiVietId(id);
        return ResponseEntity.ok(luotLike);
    }

    /**
     * Phương thức đếm số lượt bình luận của một bài viết
     * @param id: Id của bài viết
     *
     */
    @GetMapping("/{id}/luot-binh-luan")
    public ResponseEntity<Integer> demBinhLuan(@PathVariable Long id) {
        Integer luotBL = binhLuanRepository.countBinhLuanByBaiVietId(id);
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
    public ResponseEntity<LuuBaiViet> luuBaiVietVaoDSLuu(@RequestBody BaiViet baiViet, @RequestBody User user) {
        LuuBaiViet_ID id = new LuuBaiViet_ID(baiViet.getId(), user.getId());
        LuuBaiViet luuBaiViet = new LuuBaiViet(id, user, baiViet);
        luuBaiVietRepository.save(luuBaiViet);
        return ResponseEntity.ok(luuBaiViet);
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
    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("id") Long idBaiViet, @RequestParam("userId")Long userId) {
        try {
            boolean isLiked = luotLike_BaiViet_Service.toggleLike(idBaiViet, userId);
            return ResponseEntity.ok(isLiked);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
