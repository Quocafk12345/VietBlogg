package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.User;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@SessionAttributes("user")
public class Giao_Dien_Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/register")
    public String sign_up() {
        return "account/register";
    }
    @GetMapping("/forgot-password")
    public String forgot_pass() {
        return "account/forgot-password";
    }

    @GetMapping("/reset-password")
    public String Reset() {
        return "account/reset-password";
    }
    @GetMapping("/verify-token")
    public String showVerifyTokenForm() {
        return "account/verify-token"; // Trả về trang nhập mã xác thực
    }

    @GetMapping("/index")
    public String index() {
        return "home";
    }

    // Profile page
    @GetMapping("/profilepage")
    public String showProfile(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        // Check if userId is provided
        if (userId == null) {
            model.addAttribute("error", "Vui lòng đăng nhập để truy cập trang cá nhân.");
            return "account/login"; // Redirect to login page if userId is not provided
        }

        // Fetch user by ID
        User user = userService.findById(userId);
        if (user != null) {
            // Add user info to the model
            model.addAttribute("user", user);

            // Calculate days since registration
            LocalDate registrationDate = user.getNgayTao();
            long daysSinceRegistration = ChronoUnit.DAYS.between(registrationDate, LocalDate.now());
            model.addAttribute("daysSinceRegistration", daysSinceRegistration);

            // Get total number of posts by user
            int totalBaiViet = baiVietService.countBaiVietByUserId(userId);
            model.addAttribute("totalBaiViet", totalBaiViet);

            // Get the list of user's posts
            List<BaiViet> baiVietList = baiVietService.getBaiVietByUserId(userId);
            model.addAttribute("baiVietList", baiVietList);

        } else {
            // Handle case where user is not found
            model.addAttribute("error", "User không tồn tại.");
            return "account/login"; // Redirect to login if the user is not found
        }

        return "account/profilepage"; // Return to profile page
    }

}
