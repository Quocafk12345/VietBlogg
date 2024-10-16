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
    public String loginUser(@RequestParam("identifier") String identifier,
                            @RequestParam("password") String password,
                            Model model) {
        User user = null;

        // Check if the identifier is an email or phone number
        if (identifier.contains("@")) {
            user = userService.findByEmail(identifier); // Search by email
        } else {
            user = userService.findByDienThoai(identifier); // Search by phone number
        }

        if (user != null && user.getMatKhau().equals(password)) {
            // If password is correct, redirect to the profile page with userId as a query parameter
            return "redirect:/profilepage?userId=" + user.getUserId();
        } else {
            // If login fails, show an error message
            model.addAttribute("error", "Email/Số điện thoại hoặc mật khẩu không chính xác");
            return "account/login"; // Return to login page with an error message
        }
    }

}


