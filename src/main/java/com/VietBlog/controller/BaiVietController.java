package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import com.VietBlog.entity.User;
import com.VietBlog.repository.*;
import com.VietBlog.service.BaiVietService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserRepository userRepository;

    @Autowired
    public BaiVietController(BaiVietService baiVietService, BinhLuanRepository binhLuanRepository,
                             LuotLike_BaiViet_Repository luotLikeRepository, LuuBaiVietRepository luuBaiVietRepository,
                             UserRepository userRepository) {
        this.baiVietService = baiVietService;
        this.binhLuanRepository = binhLuanRepository;
        this.luotLikeRepository = luotLikeRepository;
        this.luuBaiVietRepository = luuBaiVietRepository;
        this.userRepository = userRepository;
    }

    /**
     * Phương thức lấy tất cả bài đăng
     *
     */
    @GetMapping
    public ResponseEntity<List<BaiViet>> findAll() {
        return ResponseEntity.ok(baiVietService.getAllBaiViet());
    }

    /**
     * Phương thức đếm số lượt like của một bài viết
     * @param id: Id của bài viết
     *
     */
    @GetMapping("/{id}/luot-like")
    public ResponseEntity<Integer> demLuotThich(@PathVariable Long id) {
        Integer luotLike = luotLikeRepository.countLuotLike_BaiVietByBaiVietId(id);
        return ResponseEntity.ok(luotLike);
    }

    /**
     * Phương thức đếm số lượt bình luận của một bài viết
     * @param idBaiViet: Id của bài viết
     *
     */
    @GetMapping("/{id}/luot-binh-luan")
    public ResponseEntity<Integer> demBinhLuan(@PathVariable Long idBaiViet) {
        Integer luotBL = binhLuanRepository.countBinhLuanByBaiVietId(idBaiViet);
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
            BaiViet baiVietMoi = baiVietService.themBaiViet(baiViet); // Sử dụng BaiVietService
            return ResponseEntity.ok(baiVietMoi);
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
}
