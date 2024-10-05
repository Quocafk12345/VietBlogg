package com.VietBlog.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class accountController {
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
	
}
