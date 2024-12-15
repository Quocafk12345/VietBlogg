package com.VietBlog.controller;

import com.VietBlog.constraints.BaiViet.TrangThai_BaiViet;
import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.User;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.BinhLuanService;
import com.VietBlog.service.LuotLike_BaiViet_Service;
import com.VietBlog.service.LuuBaiVietService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaiVietControllerTest {

    @Mock
    private BaiVietService baiVietService;

    @Mock
    private LuuBaiVietService luuBaiVietService;

    @Mock
    private LuotLike_BaiViet_Service luotLike_BaiViet_Service;

    @Mock
    private BinhLuanService binhLuanService;

    @InjectMocks
    private BaiVietController baiVietController;

    @Test
    public void testFindAll() {
        // Giả lập dữ liệu trả về từ service
        List<BaiViet> baiVietList = new ArrayList<>();
        baiVietList.add(new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG));
        baiVietList.add(new BaiViet(2L, "làm sao để thành Tester?", "Học và hành", TrangThai_BaiViet.DA_DANG));
        when(baiVietService.getAllBaiViet()).thenReturn(baiVietList);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.findAll();

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiVietList, response.getBody());

        // In kết quả
        System.out.println("testFindAll: " + response.getBody());
    }

    @Test
    public void testFindById() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập dữ liệu trả về từ service
        BaiViet baiViet = new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG);
        when(baiVietService.getBaiVietById(id)).thenReturn(baiViet);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<BaiViet> response = baiVietController.findById(id);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiViet, response.getBody());

        System.out.println("testFindById: " + response.getBody());
    }

    @Test
    public void testFindByUserId_coBaiViet() {
        // Giả lập userId
        Long userId = 1L;

        // Giả lập dữ liệu trả về từ service
        List<BaiViet> baiVietList = new ArrayList<>();
        baiVietList.add(new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG));
        when(baiVietService.getBaiVietByUserId(userId)).thenReturn(baiVietList);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.findByUserId(userId);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiVietList, response.getBody());

        System.out.println("testFindByUserId_coBaiViet: " + response.getBody());
    }

    @Test
    public void testFindByUserId_khongCoBaiViet() {
        // Giả lập userId
        Long userId = 1L;

        // Giả lập dữ liệu trả về từ service (danh sách rỗng)
        when(baiVietService.getBaiVietByUserId(userId)).thenReturn(new ArrayList<>());

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.findByUserId(userId);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // In kết quả
        System.out.println("testFindByUserId_khongCoBaiViet: " + response.getBody());
    }

    @Test
    public void testDemLuotThich() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập số lượt thích trả về từ service
        int luotThich = 10;
        when(luotLike_BaiViet_Service.demLuotLikeTheoBaiViet(id)).thenReturn(luotThich);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<Integer> response = baiVietController.demLuotThich(id);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(luotThich, response.getBody().intValue());

        // In kết quả
        System.out.println("testDemLuotThich: " + response.getBody());
    }

    @Test
    public void testDemBinhLuan() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập số lượt bình luận trả về từ service
        int luotBinhLuan = 5;
        when(binhLuanService.demLuotBinhLuan(id)).thenReturn(luotBinhLuan);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<Integer> response = baiVietController.demBinhLuan(id);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(luotBinhLuan, response.getBody().intValue());


        // In kết quả
        System.out.println("testDemBinhLuan: " + response.getBody());
    }

    @Test
    public void testFindByKeyword_coBaiViet() {
        // Giả lập keyword
        String keyword = "test";

        // Giả lập dữ liệu trả về từ service
        List<BaiViet> baiVietList = new ArrayList<>();
        baiVietList.add(new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG));
        when(baiVietService.getBaiVietByKeyword(keyword)).thenReturn(baiVietList);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.findByKeyword(keyword);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiVietList, response.getBody());

        // In kết quả
        System.out.println("testFindByKeyword_coBaiViet: " + response.getBody());
    }

    @Test
    public void testFindByKeyword_khongCoBaiViet() {
        // Giả lập keyword
        String keyword = "test";

        // Giả lập dữ liệu trả về từ service (danh sách rỗng)
        when(baiVietService.getBaiVietByKeyword(keyword)).thenReturn(new ArrayList<>());

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.findByKeyword(keyword);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // In kết quả
        System.out.println("testFindByKeyword_khongCoBaiViet: " + response.getBody());
    }

    @Test
    public void testDangBaiViet_thanhCong() {
        // Giả lập dữ liệu bài viết
        BaiViet baiViet = new BaiViet(null, "Tester có dễ không?", "Không khó lắm đâu bạn!...", null);

        // Giả lập kết quả trả về từ service
        when(baiVietService.themBaiViet(any(BaiViet.class))).thenReturn(baiViet);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<BaiViet> response = baiVietController.dangBaiViet(baiViet);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiViet, response.getBody());
        assertEquals(TrangThai_BaiViet.DA_DANG, response.getBody().getTrangThai());

        // In kết quả
        System.out.println("testDangBaiViet_thanhCong: " + response.getBody());
    }

    @Test
    public void testDangBaiViet_thatBai() {
        // Giả lập dữ liệu bài viết
        BaiViet baiViet = new BaiViet(null, "Tester có dễ không?", "Không khó lắm đâu bạn!...", null);

        // Giả lập service throw exception
        when(baiVietService.themBaiViet(any(BaiViet.class))).thenThrow(new RuntimeException("Lỗi"));

        // Gọi phương thức cần kiểm thử
        ResponseEntity<BaiViet> response = baiVietController.dangBaiViet(baiViet);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // In kết quả
        System.out.println("testDangBaiViet_thatBai: " + response.getBody());
    }

    @Test
    public void testLuuBaiVietVaoDSLuu() {
        // Giả lập dữ liệu bài viết và user
        BaiViet baiViet = new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG);
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");

        // Gọi phương thức cần kiểm thử
        ResponseEntity<?> response = baiVietController.luuBaiVietVaoDSLuu(baiViet, user);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(luuBaiVietService, times(1)).luuBaiViet(user.getId(), baiViet.getId());

        // In kết quả
        System.out.println("testLuuBaiVietVaoDSLuu: " + response.getStatusCode());
    }

    @Test
    public void testUpdate_thanhCong() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập dữ liệu bài viết
        BaiViet baiViet = new BaiViet(id, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG);

        // Giả lập kết quả trả về từ service
        when(baiVietService.capNhatBaiViet(eq(id), any(BaiViet.class))).thenReturn(baiViet);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<BaiViet> response = baiVietController.update(id, baiViet);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiViet, response.getBody());

        // In kết quả
        System.out.println("testUpdate_thanhCong: " + response.getBody());
    }

    @Test
    public void testUpdate_khongTimThayBaiViet() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập dữ liệu bài viết
        BaiViet baiViet = new BaiViet(id, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG);

        // Giả lập service throw exception
        when(baiVietService.capNhatBaiViet(eq(id), any(BaiViet.class))).thenThrow(new RuntimeException("Không tìm thấy bài viết"));

        // Gọi phương thức cần kiểm thử
        ResponseEntity<BaiViet> response = baiVietController.update(id, baiViet);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // In kết quả
        System.out.println("testUpdate_khongTimThayBaiViet: " + response.getBody());
    }

    @Test
    public void testDelete_thanhCong() {
        // Giả lập id bài viết
        Long id = 1L;

        // Gọi phương thức cần kiểm thử
        ResponseEntity<Void> response = baiVietController.delete(id);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(baiVietService, times(1)).xoaBaiViet(id);

        // In kết quả
        System.out.println("testDelete_thanhCong: " + response.getStatusCode());
    }

    @Test
    public void testDelete_khongTimThayBaiViet() {
        // Giả lập id bài viết
        Long id = 1L;

        // Giả lập service throw exception
        doThrow(new RuntimeException("Không tìm thấy bài viết")).when(baiVietService).xoaBaiViet(id);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<Void> response = baiVietController.delete(id);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // In kết quả
        System.out.println("testDelete_khongTimThayBaiViet: " + response.getStatusCode());
    }

    @Test
    public void testToggleLike_thanhCong() {
        // Giả lập idBaiViet và userId
        Long idBaiViet = 1L;
        Long userId = 1L;

        // Giả lập kết quả trả về từ service
        when(luotLike_BaiViet_Service.toggleLike(idBaiViet, userId)).thenReturn(true);

        // Gọi phương thức cần kiểm thử
        boolean result = baiVietController.toggleLike(idBaiViet, userId);

        // Kiểm tra kết quả trả về
        assertTrue(result);

        // In kết quả
        System.out.println("testToggleLike_thanhCong: " + result);
    }

    @Test
    public void testKiemTraLike_thanhCong() {
        // Giả lập idBaiViet và userId
        Long idBaiViet = 1L;
        Long userId = 1L;

        // Giả lập kết quả trả về từ service
        when(luotLike_BaiViet_Service.kiemTraLikeBaiViet(userId, idBaiViet)).thenReturn(true);

        // Gọi phương thức cần kiểm thử
        boolean result = baiVietController.kiemTraLike(idBaiViet, userId);

        // Kiểm tra kết quả trả về
        assertTrue(result);

        // In kết quả
        System.out.println("testKiemTraLike_thanhCong: " + result);
    }

    @Test
    public void testLayDanhSachBaiVietCuaUserTrongNhom() {
        // Giả lập nhomId và userId
        Long nhomId = 1L;
        Long userId = 1L;

        // Giả lập dữ liệu trả về từ service
        List<BaiViet> baiVietList = new ArrayList<>();
        baiVietList.add(new BaiViet(1L, "Tester có dễ không?", "Không khó lắm đâu bạn!...", TrangThai_BaiViet.DA_DANG));
        when(baiVietService.getBaiVietByNhomIdAndUserId(nhomId, userId)).thenReturn(baiVietList);

        // Gọi phương thức cần kiểm thử
        ResponseEntity<List<BaiViet>> response = baiVietController.layDanhSachBaiVietCuaUserTrongNhom(nhomId, userId);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baiVietList, response.getBody());

        System.out.println("testLayDanhSachBaiVietCuaUserTrongNhom: " + response.getBody());

        // In kết quả
        System.out.println("testLayDanhSachBaiVietCuaUserTrongNhom: " + response.getBody());
    }
}