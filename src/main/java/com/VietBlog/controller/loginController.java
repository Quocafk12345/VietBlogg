package com.VietBlog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;

@Controller
public class loginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model) {
        User user = userService.findByEmail(email); // Tìm người dùng theo email
        if (user != null && user.getMatKhau().equals(password)) {
            // Kiểm tra mật khẩu chính xác, chuyển hướng tới trang chủ
            return "redirect:/home"; // Chuyển đến trang chủ nếu đăng nhập thành công
        } else {
            // Nếu sai email hoặc mật khẩu, hiển thị lỗi
            model.addAttribute("error", "Email hoặc mật khẩu không chính xác");
            return "login"; // Trả về trang login.html với thông báo lỗi
        }
    }

}

