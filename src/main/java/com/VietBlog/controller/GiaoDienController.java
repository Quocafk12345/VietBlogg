package com.VietBlog.controller;

import com.VietBlog.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("currentUser")
public class GiaoDienController {

	// trang đăng nhập
	@GetMapping("/dang-nhap")
	public String login() {
		return "account/dang-nhap";
	}

	@PostMapping("/dang-nhap-thanh-cong") // Endpoint mới để lưu User vào session
	public String loginSuccess(@RequestBody User user, Model model, @ModelAttribute("currentUser") User currentUser) {
		model.addAttribute("currentUser", user);
		return "redirect:/index"; // Redirect đến trang index
	}

	// trang chi tiết nhóm
	@GetMapping("/nhom/chi-tiet/{idNhom}")
	public String chiTietNhom(@PathVariable Long idNhom, Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			model.addAttribute("idNhom", idNhom);
			return "page/chi-tiet-nhom";
		} else return "redirect:/dang-nhap";
	}

	@GetMapping("/nhom")
	public String trangNhom(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "page/kham-pha-nhom";
		} else return "redirect:/dang-nhap";
	}

	@GetMapping("/danh-sach-luu")
	public String danhSachLuu(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "page/danh-sach-luu";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/tao-nhom")
	public String hienThiTrangTaoNhom(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {

			return "page/tao-nhom";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/cong-dong") // Thêm value attribute
	public String congDongPage(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {

			return "include/cong-dong";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/nhap")
	public String baiDaNhap(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "page/bai-nhap";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/tim-kiem")
	public String KetQuaTimKiem(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "page/ket-qua-tim-kiem";
		} else return "redirect:/dang-nhap";

	}


	@GetMapping("/dang-ky")
	public String sign_up(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/dang-ky";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/quen-mat-khau")
	public String forgot_pass(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/quen-mat-khau";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/nhap-mat-khau-moi")
	public String reset(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/nhap-mat-khau-moi";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/nhap-otp")
	public String showVerifyTokenForm(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/nhap-otp"; // Trả về trang nhập mã xác thực
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/dang-bai")
	public String postPage(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "page/dang-bai";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/bai-viet/chinh-sua/{id}")
	public String postPage(@PathVariable Long id, Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			model.addAttribute("idBaiViet", id);
			return "page/dang-bai";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/index")
	public String index(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "trang-chu";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/bai-viet/{id}")
	public String detail(@PathVariable Long id, Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			model.addAttribute("idBaiViet", id);
			return "page/chi-tiet-bai-viet";
		} else return "redirect:/dang-nhap";

	}

	@GetMapping("/cai-dat")
	public String hienThiCaiDat(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/trang-ca-nhan";
		} else return "redirect:/dang-nhap";
	}

	// Profile page
	@GetMapping("/trang-ca-nhan/{userId}")
	public String showProfile(Model model, @ModelAttribute("currentUser") User currentUser) {
		if (currentUser != null) {
			return "account/trang-ca-nhan";
		} else return "redirect:/dang-nhap";
	}


}
