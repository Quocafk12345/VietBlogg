package com.VietBlog.controller;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.User;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

@Controller
@SessionAttributes("currentUser")
public class GiaoDienController {

    private final UserService userService;

    private final BaiVietService baiVietService;

    @Autowired
    public GiaoDienController(UserService userService, BaiVietService baiVietService) {
        this.userService = userService;
        this.baiVietService = baiVietService;
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @PostMapping("/login-success") // Endpoint mới để lưu User vào session
    public String loginSuccess(@RequestBody User user, Model model) {
        model.addAttribute("currentUser", user);
        return "redirect:/index"; // Redirect đến trang index
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete(); // Hủy session
        return "redirect:/login"; // Redirect đến trang đăng nhập
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

    @GetMapping("/dang-bai")
    public String postPage(Model model) {
        model.addAttribute("bv",new BaiViet());
        return "postPage";
    }


    @GetMapping("/index")
    public String index() {
        return "home";
    }

    @GetMapping("/chi-tiet-bai-viet/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("baiVietId", id);
        return "ChiTietBaiViet";
    }

    @GetMapping("/cai-dat")
    public String hienThiCaiDat(Model model, HttpServletRequest request, TimeZone timeZone) {
        try {
            User user = (User) model.getAttribute("user"); // Thay bằng cách lấy từ session
            if (user == null) {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin người dùng.");
                return "error";
            }

            // Thêm thông tin giao diện vào model
            model.addAttribute("user", user);
            model.addAttribute("mauNen", user.getMauNen());
            model.addAttribute("fontChu", user.getFontChu());
            model.addAttribute("coChu", user.getCoChu());

            return "CaiDat";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi xử lý yêu cầu.");
            return "error";
        }
    }
    // Profile page
    @GetMapping("/trang-ca-nhan")
    public String showProfile(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        // Check if userId is provided
        if (userId == null) {
            model.addAttribute("error", "Vui lòng đăng nhập để truy cập trang cá nhân.");
            return "redirect:/login"; // Redirect to login page if userId is not provided
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
            return "redirect:/login"; // Redirect to login if the user is not found
        }

        return "account/profilepage"; // Return to profile page
    }


}
