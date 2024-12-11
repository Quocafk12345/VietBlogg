package com.VietBlog.controller;

import com.VietBlog.entity.User;
import com.VietBlog.service.BaiVietService;
import com.VietBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    // trang đăng nhập
    @GetMapping("/dang-nhap")
    public String login() {
        return "account/dang-nhap";
    }

    @PostMapping("/dang-nhap-thanh-cong") // Endpoint mới để lưu User vào session
    public String loginSuccess(@RequestBody User user, Model model) {
        model.addAttribute("currentUser", user);
        return "redirect:/index"; // Redirect đến trang index
    }

    // trang chi tiết nhóm
    @GetMapping("/nhom/chi-tiet/{idNhom}")
    public String chiTietNhom(@PathVariable Long idNhom, Model model) {
        model.addAttribute("idNhom", idNhom);
        return "page/chi-tiet-nhom";
    }

    @GetMapping("/nhom")
    public String trangNhom() {
        return "page/kham-pha-nhom";
    }


    @GetMapping("/tao-nhom")
    public String hienThiTrangTaoNhom() {
        return "page/tao-nhom";
    }

//    @GetMapping("/cong-dong") // Thêm value attribute
//    public String congDongPage() {
//        return "cong-dong";
//    }

    @GetMapping("/tim-kiem")
    public String KetQuaTimKiem() {
        return "page/ket-qua-tim-kiem";
    }


    @GetMapping("/dang-ky")
    public String sign_up() {
        return "account/dang-ky";
    }

    @GetMapping("/quen-mat-khau")
    public String forgot_pass() {
        return "account/quen-mat-khau";
    }

    @GetMapping("/nhap-mat-khau-moi")
    public String Reset() {
        return "account/nhap-mat-khau-moi";
    }

    @GetMapping("/nhap-otp")
    public String showVerifyTokenForm() {
        return "account/nhap-otp"; // Trả về trang nhập mã xác thực
    }

    @GetMapping("/dang-bai")
    public String postPage() {
        return "page/dang-bai";
    }

    @GetMapping("/index")
    public String index() {
        return "trang-chu";
    }

    @GetMapping("/bai-viet/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("idBaiViet", id);
        return "page/chi-tiet-bai-viet";
    }

    @GetMapping("/cai-dat")
    public String hienThiCaiDat(Model model, @ModelAttribute("currentUser") User currentUser) {
        if (currentUser == null) {
            return "redirect:/dang-nhap"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        try {
            // Thêm thông tin giao diện vào model
            model.addAttribute("user", currentUser);
            model.addAttribute("mauNen", currentUser.getTheme());

            return "cai-dat-ca-nhan";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi xử lý yêu cầu.");
            return "error";
        }
    }
    // Profile page
    @GetMapping("/trang-ca-nhan/{userId}")
    public String showProfile(@PathVariable(value = "userId", required = false) Long userId, Model model) {
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
            int totalBaiViet = baiVietService.demSoLuongBaiVietChoUser(userId);
            model.addAttribute("totalBaiViet", totalBaiViet);

            int totalLike = userService.countLikesByUserId(userId);
            model.addAttribute("totalLikes", totalLike);

            int totalFollow = userService.countFollowsByUserId(userId);
            model.addAttribute("totalFollows", totalFollow);
        } else {
            // Handle case where user is not found
            model.addAttribute("error", "User không tồn tại.");
            return "redirect:/dang-nhap"; // Redirect to login if the user is not found
        }

        return "account/trang-ca-nhan"; // Return to profile page
    }


}
