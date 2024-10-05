package com.VietBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;

@Controller
public class registerController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
    	 user.setVaiTro("User");
        if (userService.isEmailExists(user.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại");
            return "account/register";
        }

        userService.saveUser(user);
        return "redirect:/login"; // Chuyển hướng tới trang đăng nhập sau khi đăng ký thành công
    }
}

