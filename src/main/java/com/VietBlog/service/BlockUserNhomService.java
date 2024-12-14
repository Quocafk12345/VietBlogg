package com.VietBlog.service;

import com.VietBlog.constraints.ThanhVien.VaiTro_ThanhVien;
import com.VietBlog.entity.BlockUserNhom;
import com.VietBlog.entity.BlockUserNhomId;
import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import com.VietBlog.repository.BlockUserNhomRepository;
import com.VietBlog.repository.NhomRepository;
import com.VietBlog.repository.ThanhVienRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BlockUserNhomService {
    @Autowired
    private BlockUserNhomRepository blockUserNhomRepository;

    @Autowired
    private NhomRepository nhomRepository;

    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThanhVienService thanhVienService;

    @Transactional
    public void chanNguoiDung(Long nhomId, Long userId, Long blockedUserId) {
        if (userId.equals(blockedUserId)) {
            throw new RuntimeException("Bạn không thể tự chặn chính mình.");
        }

        List<ThanhVien> thanhViens = thanhVienRepository.findAllById(List.of(
                new ThanhVienId(nhomId, userId),
                new ThanhVienId(nhomId, blockedUserId)
        ));

        // 1. Kiểm tra xem người dùng BỊ CHẶN có phải là thành viên của nhóm hay không
        long soLuongThanhVien = thanhVienRepository.countById_IdNhomAndId_UserId(nhomId, blockedUserId);
        if (soLuongThanhVien == 0) {
            throw new RuntimeException("Người dùng bị chặn không phải là thành viên của nhóm.");
        }

        // 2. Kiểm tra xem người dùng BỊ CHẶN đã bị chặn hay chưa
        if (blockUserNhomRepository.existsById(new BlockUserNhomId(userId, blockedUserId, nhomId))) {
            throw new RuntimeException("Người dùng đã bị chặn khỏi nhóm này.");
        }

        ThanhVien quanTriVien = thanhViens.stream()
                .filter(tv -> tv.getId().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        ThanhVien thanhVienBiChan = thanhViens.stream()
                .filter(tv -> tv.getId().getUserId().equals(blockedUserId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng bị chặn trong nhóm."));

        if (!quanTriVien.getVaiTro().equals(VaiTro_ThanhVien.QUAN_TRI_VIEN)
                && !quanTriVien.getVaiTro().equals(VaiTro_ThanhVien.CHU_NHOM)) {
            throw new RuntimeException("Bạn không có quyền chặn người dùng khỏi nhóm này.");
        }

        if (thanhVienBiChan.getVaiTro().equals(VaiTro_ThanhVien.QUAN_TRI_VIEN)
                || thanhVienBiChan.getVaiTro().equals(VaiTro_ThanhVien.CHU_NHOM)) {
            throw new RuntimeException("Không thể chặn quản trị viên hoặc chủ nhóm.");
        }

        BlockUserNhomId blockUserNhomId = new BlockUserNhomId(userId, blockedUserId, nhomId);
        BlockUserNhom blockUserNhom = new BlockUserNhom(
                blockUserNhomId,
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")),
                userRepository.findById(blockedUserId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng bị chặn")),
                nhomRepository.findById(nhomId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm")),
                LocalDateTime.now(),
                null
        );
        blockUserNhomRepository.save(blockUserNhom);

        thanhVienService.xoaThanhVien(nhomId, blockedUserId); // Xóa blockedUserId
    }

}
