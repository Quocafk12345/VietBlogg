package com.VietBlog.controller;

import com.VietBlog.constraints.User.VaiTro_User;
import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DangKyController {

    @Autowired
    private UserService userService;

    @PostMapping("/dang-ky")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        user.setVaiTro(VaiTro_User.USER);
        if (userService.timTheoEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại");
            return "dang-ky";
        }
        user.setHinhDaiDien(null);
        // Handle the optional Ngày sinh field
        if (user.getNgaySinh() == null) {
            // You can set a default date or leave it as null
            user.setNgaySinh(null);
        }

        userService.dangKy(user);
        return "redirect:/dang-nhap"; // Redirect to login page after successful registration
    }
}

