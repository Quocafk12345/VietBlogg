package com.VietBlog.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VietBlog.entity.User;
import com.VietBlog.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // Trả về null nếu không tìm thấy
    }
    
    public User saveUser(User user) {
        user.setNgayTao(LocalDate.now()); // Gán ngày tạo là ngày hiện tại
        return userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public User findByDienThoai(String dienThoai) {
        return userRepository.findByDienThoai(dienThoai).orElse(null);
    }


    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
}

