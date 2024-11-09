package com.VietBlog.controller;

import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import com.VietBlog.entity.User;
import com.VietBlog.repository.*;
import com.VietBlog.service.BaiVietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bai-viet")
public class BaiVietController {

    @Value("${uploadDir}")
    private String uploadFolder;

    private final BaiVietService baiVietService;
    private final BinhLuanRepository binhLuanRepository;
    private final LuotLike_BaiViet_Repository luotLikeRepository;
    private final LuuBaiVietRepository luuBaiVietRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public BaiVietController(BaiVietService baiVietService, BinhLuanRepository binhLuanRepository,
                             LuotLike_BaiViet_Repository luotLikeRepository, LuuBaiVietRepository luuBaiVietRepository) {
        this.baiVietService = baiVietService;
        this.binhLuanRepository = binhLuanRepository;
        this.luotLikeRepository = luotLikeRepository;
        this.luuBaiVietRepository = luuBaiVietRepository;
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
    
    @PostMapping("/dang-bai")
    @Transactional
    public @ResponseBody ResponseEntity<?> dangBaiViet(@RequestParam("Tieu_De")String tieuDe, @RequestParam("NoiDung")String noiDung, @RequestParam("Trang_Thai")String trangThaiStr, @RequestParam("thumbnail")MultipartFile file, Model model, HttpServletRequest request, HttpSession session) {
        try {
            User user = (User) session.getAttribute("id");
            if(user == null){
                return new ResponseEntity<>("Bạn cần đăng nhập để tạo bài viết", HttpStatus.UNAUTHORIZED);
            }
            TrangThai_BaiViet trangThai = TrangThai_BaiViet.valueOf(trangThaiStr);

            if(tieuDe.isEmpty()||noiDung.isEmpty()){
                return new ResponseEntity<>("Tiêu đề và nội dung không được để trống.", HttpStatus.BAD_REQUEST);
            }
            String imageData = null;
            if(file != null && !file.isEmpty()) {
                String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
                log.info("uploadDirectory::" + uploadDirectory);
                String fileName = file.getOriginalFilename();
                String filePath = Paths.get(uploadDirectory, fileName).toString();

                //Tao thư mục nếu chưa có
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath))) {
                    stream.write(file.getBytes());
                } catch (Exception e) {
                    return new ResponseEntity<>("Lỗi khi tải lên hình ảnh.", HttpStatus.BAD_REQUEST);
                }
                imageData = "/uploads" + fileName;
            }
                //Lưu đường dẫn bài viết
                BaiViet baiViet = new BaiViet();
                baiViet.setTieuDe(tieuDe);
                baiViet.setNoiDung(noiDung);
                baiViet.setTrangThai(trangThai);
                baiViet.setNgayTao(new Timestamp(System.currentTimeMillis()));
                baiViet.setUser(user);
                if (imageData != null) {
                    baiViet.setThumbnail(imageData);//Gán dường dẫn hình ảnh
                }

                boolean isSaved = baiVietService.themBaiViet(baiViet);

                if (isSaved) {
                    return new ResponseEntity<>("Ba Viết đã được tạo thành công!", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Có lỗi xảy ra khi tạo bài viết.", HttpStatus.BAD_REQUEST);
                }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Trạng thái không hợp lệ.", HttpStatus.BAD_REQUEST);// Hoặc trả về thông báo lỗi cụ thể hơn
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi ệ thống", HttpStatus.INTERNAL_SERVER_ERROR);
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
