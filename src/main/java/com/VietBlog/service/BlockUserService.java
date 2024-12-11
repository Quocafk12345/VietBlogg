package com.VietBlog.service;

import com.VietBlog.entity.*;
import com.VietBlog.repository.BlockUserRepository;
import com.VietBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockUserService {
    private final BlockUserRepository blockUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public BlockUserService(BlockUserRepository blockUserRepository, UserRepository userRepository) {
        this.blockUserRepository = blockUserRepository;
        this.userRepository = userRepository;
    }

    public boolean toggleBlock(Long userId, Long userBlockId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại."));
        User currentUser = userRepository.findById(userBlockId)
                .orElseThrow(() -> new RuntimeException("Chưa login."));

        // Tạo ID cho bảng trung gian
        BlockUser_ID blockUserId = new BlockUser_ID(userId, userBlockId);

        if (blockUserRepository.existsById(blockUserId)) {
            blockUserRepository.deleteById(blockUserId);
            return false;
        } else {
            BlockUser blockUser = new BlockUser();
            blockUser.setId(blockUserId);
            blockUser.setUser(user);
            blockUser.setUserBiChan(currentUser);
            blockUserRepository.save(blockUser);
            return true;
        }
    }
}
