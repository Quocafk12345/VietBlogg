package com.VietBlog.service;

import com.VietBlog.entity.BlockUserNhom;
import com.VietBlog.entity.BlockUserNhomId;
import com.VietBlog.repository.BlockUserNhomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockUserNhomService {

    @Autowired
    private BlockUserNhomRepository blockUserNhomRepository;

    public void chanNguoiDung(Long nhomId, Long userId, Long blockedUserId) {
        BlockUserNhomId blockUserNhomId = new BlockUserNhomId(userId, blockedUserId, nhomId);

        if (blockUserNhomRepository.existsById(blockUserNhomId)) {
            throw new RuntimeException("Người dùng đã bị chặn khỏi nhóm này.");
        }

        BlockUserNhom blockUserNhom = new BlockUserNhom();
        blockUserNhom.setId(blockUserNhomId);
        // ... (set các thuộc tính khác)
        blockUserNhom.setNgayBatDau(LocalDateTime.now()); // Set ngày bắt đầu chặn

        blockUserNhomRepository.save(blockUserNhom);
    }

    public void boChanNguoiDung(Long nhomId, Long userId, Long blockedUserId) {
        BlockUserNhomId blockUserNhomId = new BlockUserNhomId(userId, blockedUserId, nhomId);
        BlockUserNhom blockUserNhom = blockUserNhomRepository.findById(blockUserNhomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi chặn."));

        blockUserNhom.setNgayKetThuc(LocalDateTime.now()); // Set ngày kết thúc chặn

        blockUserNhomRepository.save(blockUserNhom);
    }

    // Các phương thức mới để truy vấn lịch sử chặn (nếu cần)
    public List<BlockUserNhom> layLichSuChanCuaNguoiDung(Long userId, Long nhomId) {
        return blockUserNhomRepository.findByUser_IdAndNhom_IdOrderByNgayBatDauDesc(userId, nhomId);
    }

    // ... (các phương thức khác)
}