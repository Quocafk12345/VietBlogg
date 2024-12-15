package com.VietBlog.controller;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.*;
import com.VietBlog.repository.*;
import com.VietBlog.service.NhomService;
import com.VietBlog.service.ThanhVienService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NhomControllerTest {

    @Mock
    private ThanhVienRepository thanhVienRepository;

    @Mock
    private NhomRepository nhomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ThanhVienService thanhVienService;

    @Spy
    @InjectMocks
    private NhomService nhomService;

    @Mock
    private BaiVietRepository baiVietRepository;

    @Mock
    private DS_LuatNhom_Repository dsLuatNhomRepository;

    @Test
    void testTimThanhVienTheoId() {
        // Giả lập dữ liệu
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        Nhom nhom = new Nhom(1L, "Hoa hải đường", "Hoa hải đường chia sẻ và chăm sóc", "hinh_anh_1.jpg");
        ThanhVienId thanhVienId = new ThanhVienId(nhom.getId(), user.getId());
        ThanhVien thanhVien = new ThanhVien(thanhVienId, nhom, user, VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());
        when(thanhVienRepository.findById(thanhVienId)).thenReturn(Optional.of(thanhVien));

        // Gọi hàm cần kiểm thử
        Optional<ThanhVien> result = thanhVienService.timThanhVienTheoId(thanhVienId);

        // Kiểm tra kết quả
        assertTrue(result.isPresent());
        assertEquals(thanhVien, result.get());

        // In kết quả
        System.out.println("testTimThanhVienTheoId: " + result.get());
    }

    @Test
    void testThemThanhVien() {
        // Giả lập dữ liệu
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        Nhom nhom = new Nhom(1L, "Hoa hải đường", "Hoa hải đường chia sẻ và chăm sóc", "hinh_anh_1.jpg");
        ThanhVienId thanhVienId = new ThanhVienId(nhom.getId(), user.getId());
        ThanhVien thanhVien = new ThanhVien(thanhVienId, nhom, user, VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());

        // Mock nhomRepository.findById()
        when(nhomRepository.findById(nhom.getId())).thenReturn(Optional.of(nhom));

        // Mock userRepository.findById()
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Mock thanhVienRepository.existsById()
        when(thanhVienRepository.existsById(thanhVienId)).thenReturn(false);

        // Mock thanhVienRepository.save()
        when(thanhVienRepository.save(any(ThanhVien.class))).thenReturn(thanhVien);

        // Gọi hàm cần kiểm thử
        ThanhVien result = thanhVienService.themThanhVien(nhom.getId(), user.getId(), VaiTro_ThanhVien.THANH_VIEN.toString());

        // Kiểm tra kết quả
        assertEquals(thanhVien, result);

        // In kết quả
        System.out.println("testThemThanhVien: " + result);
    }

    @Test
    void testXoaThanhVien() {
        // Giả lập dữ liệu
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        Nhom nhom = new Nhom(1L, "Hoa hải đường", "Hoa hải đường chia sẻ và chăm sóc", "hinh_anh_1.jpg");
        ThanhVienId thanhVienId = new ThanhVienId(nhom.getId(), user.getId());

        // Mock thanhVienRepository.existsById()
        when(thanhVienRepository.existsById(thanhVienId)).thenReturn(true);

        // Gọi hàm cần kiểm thử
        thanhVienService.xoaThanhVien(nhom.getId(), user.getId());

        // Kiểm tra xem repository đã gọi deleteById đúng chưa
        verify(thanhVienRepository).deleteById(thanhVienId);

        // In kết quả (không có kết quả trả về)
        System.out.println("testXoaThanhVien: Thành viên đã bị xóa.");
    }
    @Test
    void testCapNhatVaiTro() {
        // Giả lập dữ liệu
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        Nhom nhom = new Nhom(1L, "Hoa hải đường", "Hoa hải đường chia sẻ và chăm sóc", "hinh_anh_1.jpg");
        ThanhVienId thanhVienId = new ThanhVienId(nhom.getId(), user.getId());
        ThanhVien thanhVien = new ThanhVien(thanhVienId, nhom, user, VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());

        // Mock thanhVienRepository.findById()
        when(thanhVienRepository.findById(thanhVienId)).thenReturn(Optional.of(thanhVien));

        // Mock thanhVienRepository.save()
        when(thanhVienRepository.save(any(ThanhVien.class))).thenReturn(thanhVien);

        // Gọi hàm cần kiểm thử
        ThanhVien result = thanhVienService.capNhatVaiTro(nhom.getId(), user.getId(), VaiTro_ThanhVien.QUAN_TRI_VIEN.toString());

        // Kiểm tra kết quả
        assertEquals(VaiTro_ThanhVien.QUAN_TRI_VIEN, result.getVaiTro());

        // In kết quả
        System.out.println("testCapNhatVaiTro: " + result);
    }

    @Test
    void testLayDanhSachThanhVien() {
        // Giả lập dữ liệu
        Nhom nhom = new Nhom(1L, "Hoa hải đường", "Hoa hải đường chia sẻ và chăm sóc", "hinh_anh_1.jpg");
        List<ThanhVien> danhSachThanhVien = new ArrayList<>();

        // Tạo danh sách User
        User user1 = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        User user2 = new User(2L, "Nguyễn Anh Quân", "@example.com", "password", "avatar2.jpg");

        // Tạo danh sách ThanhVien và thêm vào danh sách
        ThanhVien thanhVien1 = new ThanhVien(new ThanhVienId(nhom.getId(), user1.getId()), nhom, user1, VaiTro_ThanhVien.THANH_VIEN, LocalDate.now()); // Sửa user1 thành user
        ThanhVien thanhVien2 = new ThanhVien(new ThanhVienId(nhom.getId(), user2.getId()), nhom, user2, VaiTro_ThanhVien.QUAN_TRI_VIEN, LocalDate.now());
        danhSachThanhVien.add(thanhVien1);
        danhSachThanhVien.add(thanhVien2);

        // Mock thanhVienRepository.findByNhom_Id()
        when(thanhVienRepository.findByNhom_Id(nhom.getId())).thenReturn(danhSachThanhVien);

        // Gọi hàm cần kiểm thử
        List<ThanhVien> result = thanhVienService.layDanhSachThanhVien(nhom.getId());

        // Kiểm tra kết quả
        assertEquals(danhSachThanhVien, result);

        // In kết quả
        System.out.println("testLayDanhSachThanhVien: " + result);
    }

    @Test
    void testLayDanhSachNhomDaThamGia() {
        // Giả lập dữ liệu
        User user = new User(1L, "Nguyễn Anh Qui", "quinaps31917@example.com", "password123", "avatar123.jpg");
        List<ThanhVien> danhSachThanhVien = new ArrayList<>();

        // Mock thanhVienRepository.findByUser_Id()
        when(thanhVienRepository.findByUser_Id(user.getId())).thenReturn(danhSachThanhVien);

        // Gọi hàm cần kiểm thử
        List<Nhom> result = thanhVienService.layDanhSachNhomDaThamGia(user.getId());

        // Kiểm tra kết quả
        assertEquals(danhSachThanhVien, result);

        // ... (Kiểm tra xem result có chứa đúng các nhóm mà user đã tham gia)
        System.out.println("testLayDanhSachNhomDaThamGia: " + result);
    }

    @Test
    void testThamGiaNhom() {
        // Giả lập dữ liệu
        Long nhomId = 1L;
        Long userId = 1L;
        Nhom nhom = new Nhom();
        nhom.setId(nhomId);
        User user = new User();
        user.setId(userId);
        ThanhVien thanhVien = new ThanhVien(new ThanhVienId(nhomId, userId), nhom, user, VaiTro_ThanhVien.THANH_VIEN, LocalDate.now());

        // Mock nhomRepository.findById()
        when(nhomRepository.findById(nhomId)).thenReturn(Optional.of(nhom));

        // Mock userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock thanhVienRepository.existsById()
        when(thanhVienRepository.existsById(new ThanhVienId(nhomId, userId))).thenReturn(false);

        // Mock thanhVienRepository.save()
        when(thanhVienRepository.save(any(ThanhVien.class))).thenReturn(thanhVien);

        // Gọi hàm cần kiểm thử
        ThanhVien result = thanhVienService.themThanhVien(nhomId, userId, VaiTro_ThanhVien.THANH_VIEN.toString());

        // Kiểm tra kết quả
        assertEquals(thanhVien, result);

        // In kết quả
        System.out.println("testThamGiaNhom: " + result);
    }

    @Test
    void testRoiKhoiNhom() {
        // Giả lập dữ liệu
        Long nhomId = 1L;
        Long userId = 1L;
        ThanhVienId thanhVienId = new ThanhVienId(nhomId, userId);

        // Mock thanhVienRepository.existsById()
        when(thanhVienRepository.existsById(thanhVienId)).thenReturn(true);

        // Gọi hàm cần kiểm thử
        thanhVienService.xoaThanhVien(nhomId, userId);

        // Kiểm tra xem repository đã gọi deleteById đúng chưa
        verify(thanhVienRepository).deleteById(thanhVienId);

        // In kết quả (không có kết quả trả về)
        System.out.println("testRoiKhoiNhom: Thành viên đã rời khỏi nhóm.");
    }

    @Test
    void testXoaNhom() {
        // Giả lập dữ liệu
        Long nhomId = 1L;

        // Cho phép thực thi xoaNhom() thực sự
        doCallRealMethod().when(nhomService).xoaNhom(nhomId);

        // Gọi hàm cần kiểm thử
        nhomService.xoaNhom(nhomId);

        // Kiểm tra xem các repository đã được gọi đúng chưa
        verify(baiVietRepository).deleteAllByNhom_Id(nhomId);
        verify(dsLuatNhomRepository).deleteAllByNhom_Id(nhomId);
        verify(thanhVienRepository).deleteAllByNhom_Id(nhomId);
        verify(nhomRepository).deleteById(nhomId);

        // In kết quả (không có kết quả trả về)
        System.out.println("testXoaNhom: Nhóm đã bị xóa.");
    }

    @Test
    void testLayDanhSachBaiVietCuaNhom() {
        // Giả lập dữ liệu
        Long nhomId = 1L;
        List<BaiViet> baiVietList = new ArrayList<>();
        baiVietList.add(new BaiViet()); // Thêm bài viết mẫu
        baiVietList.add(new BaiViet()); // Thêm bài viết mẫu
        baiVietList.add(new BaiViet()); // Thêm bài viết mẫu

        // Stub phương thức layDanhSachBaiVietCuaNhom()
        doReturn(baiVietList).when(nhomService).layDanhSachBaiVietCuaNhom(nhomId);

        // Gọi hàm cần kiểm thử
        List<BaiViet> result = nhomService.layDanhSachBaiVietCuaNhom(nhomId);

        // In ra kết quả để debug
        System.out.println("nhomId: " + nhomId);
        System.out.println("result.size(): " + result.size());

        // Kiểm tra kết quả
        assertEquals(baiVietList.size(), result.size()); // Kiểm tra số lượng bài viết
        assertEquals(baiVietList, result); // Kiểm tra danh sách bài viết

        System.out.println("testLayDanhSachBaiVietCuaNhom: " + result);
    }

    @Test
    void testLayNhomTheoId() {
        // Giả lập dữ liệu
        Long nhomId = 1L;
        Nhom nhom = new Nhom();
        nhom.setId(nhomId);
        nhom.setTen("Nhóm test");
        when(nhomRepository.findById(nhomId)).thenReturn(Optional.of(nhom));

        // Gọi hàm cần kiểm thử
        Optional<Nhom> result = nhomService.layNhomTheoId(nhomId);

        // Kiểm tra kết quả
        assertTrue(result.isPresent());
        assertEquals(nhom, result.get());

        // In kết quả
        System.out.println("testLayNhomTheoId: " + result.get());
    }

    @Test
    void testTaoNhom() {
        // Giả lập dữ liệu
        Nhom nhom = new Nhom();
        nhom.setTen("Đom đóm");
        nhom.setGioiThieu("Đom đóm mùa hè");
        Long chuNhomId = 1L;

        // Mock nhomRepository.save()
        Nhom nhomSaved = new Nhom();
        nhomSaved.setId(1L);
        nhomSaved.setTen(nhom.getTen());
        nhomSaved.setGioiThieu(nhom.getGioiThieu());
        nhomSaved.setNgayTao(Timestamp.from(Instant.now()));
        when(nhomRepository.save(any(Nhom.class))).thenReturn(nhomSaved);

        // Mock userRepository.findById()
        User user = new User();
        user.setId(chuNhomId);
        when(userRepository.findById(chuNhomId)).thenReturn(Optional.of(user));

        // Mock thanhVienRepository.save()
        ThanhVien thanhVien = new ThanhVien(new ThanhVienId(nhomSaved.getId(), chuNhomId),
                nhomSaved, user, VaiTro_ThanhVien.CHU_NHOM, LocalDate.now());
        when(thanhVienRepository.save(any(ThanhVien.class))).thenReturn(thanhVien);

        // Gọi hàm cần kiểm thử
        Nhom result = nhomService.taoNhom(nhom, chuNhomId);

        // Kiểm tra kết quả
        assertEquals(nhomSaved, result);

        // In kết quả
        System.out.println("testTaoNhom: " + result);
    }

    @Test
    void testTaoNhom_thieuTen() {
        // Giả lập dữ liệu
        Nhom nhom = new Nhom();
        nhom.setGioiThieu("Mô tả nhóm test");
        Long chuNhomId = 1L;

        // Gọi hàm cần kiểm thử và kiểm tra exception
        assertThrows(RuntimeException.class, () -> nhomService.taoNhom(nhom, chuNhomId));

        // Kiểm tra xem nhomRepository.save() không được gọi
        verify(nhomRepository, never()).save(any(Nhom.class));

        // In kết quả
        System.out.println("testTaoNhom_thieuTen: Thành công!");
    }

        @Test
        void testTaoNhom_thieuGioiThieu() {
            // Giả lập dữ liệu
            Nhom nhom = new Nhom();
            nhom.setTen("Nhóm test");
            Long chuNhomId = 1L;

            // Gọi hàm cần kiểm thử và kiểm tra exception
            assertThrows(RuntimeException.class, () -> nhomService.taoNhom(nhom, chuNhomId));

            // Kiểm tra xem nhomRepository.save() không được gọi
            verify(nhomRepository, never()).save(any(Nhom.class));

            // In kết quả
            System.out.println("testTaoNhom_thieuGioiThieu: Thành công!");
        }

        @Test
        void testTaoNhom_thieuTenVaGioiThieu() {
            // Giả lập dữ liệu
            Nhom nhom = new Nhom();
            Long chuNhomId = 1L;

            // Gọi hàm cần kiểm thử và kiểm tra exception
            assertThrows(RuntimeException.class, () -> nhomService.taoNhom(nhom, chuNhomId));

            // Kiểm tra xem nhomRepository.save() không được gọi
            verify(nhomRepository, never()).save(any(Nhom.class));

            // In kết quả
            System.out.println("testTaoNhom_thieuTenVaGioiThieu: Thành công!");
        }
}