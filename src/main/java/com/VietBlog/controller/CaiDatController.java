package com.VietBlog.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.VietBlog.entity.User;
import com.VietBlog.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CaiDatController {

	@Autowired
	private UserService userService;

	@GetMapping("/CaiDat")
	public String hienThiCaiDat(Model model) {
		try {
			User user = userService.findById(1L);
			if (user == null) {
				model.addAttribute("errorMessage", "Không tìm thấy thông tin người dùng.");
				return "error";
			}
			model.addAttribute("user", user);
			return "SettingsGiaoDien/CaiDat";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Có lỗi xảy ra khi xử lý yêu cầu.");
			return "error";
		}
	}

	@PostMapping("/CaiDat/capNhatHinhAnh")
	public String capNhatHinhAnh(@RequestParam("hinhAnh") MultipartFile hinhAnh, @RequestParam("userId") Long userId,
								 Model model) throws IOException {
        if (!hinhAnh.isEmpty()) {
            // Cập nhật thông tin người dùng với tên hình ảnh mới
            User user = userService.findById(userId);

            // Lưu hình ảnh vào database
            userService.luuHinhAnh(hinhAnh, user);

            model.addAttribute("message", "Cập nhật hình ảnh thành công!");
        } else {
            model.addAttribute("error", "Vui lòng chọn hình ảnh để tải lên.");
        }
        return "redirect:/CaiDat";
	}

	@PostMapping("/CaiDat/capNhat")
	public String capNhatThongTin(@ModelAttribute User user) {
		try {
			userService.updateUser(user);
			return "redirect:/CaiDat";
		} catch (Exception e) {
			// Xử lý exception, ví dụ: log lỗi và hiển thị thông báo lỗi
			return "error";
		}
	}

	@PostMapping("/CaiDat/thayDoiMatKhau")
	public String thayDoiMatKhau(@ModelAttribute User user, @RequestParam("matKhauHienTai") String matKhauHienTai,
								 @RequestParam("matKhauMoi") String matKhauMoi, @RequestParam("xacNhanMatKhauMoi") String xacNhanMatKhauMoi,
								 Model model) {
		try {
			// Lấy thông tin người dùng từ session (hoặc từ database)
			User existingUser = userService.findByEmail("user1@example.com"); // Thay bằng cách lấy từ session

			if (existingUser == null) {
				model.addAttribute("errorMessage", "Không tìm thấy thông tin người dùng.");
				return "error";
			}

			// Kiểm tra mật khẩu hiện tại
			if (!existingUser.getMatKhau().equals(matKhauHienTai)) {
				model.addAttribute("errorMessage", "Mật khẩu hiện tại không đúng.");
				return "error";
			}

			// Kiểm tra mật khẩu mới và xác nhận mật khẩu mới
			if (!matKhauMoi.equals(xacNhanMatKhauMoi)) {
				model.addAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu mới không khớp.");
				return "error";
			}

			// Cập nhật mật khẩu
			existingUser.setMatKhau(matKhauMoi);
			userService.updateUser(existingUser);

			return "redirect:/CaiDat";

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Có lỗi xảy ra khi thay đổi mật khẩu.");
			return "error";
		}
	}

	@PostMapping("/CaiDat/capNhatThongTinCaNhan")
	public String capNhatThongTinCaNhan(@RequestParam("userId") Long userId,
										@RequestParam("dienThoai") String dienThoai, @RequestParam("ngaySinh") String ngaySinh,
										@RequestParam(value = "gioiTinh", required = false) Boolean gioiTinh, // Thêm tham số gioiTinh
										Model model) {
		try {
			java.sql.Date ngaySinhSql = java.sql.Date.valueOf(ngaySinh);
			User user = userService.findById(userId);
			user.setDienThoai(dienThoai);
			user.setNgaySinh(ngaySinhSql.toLocalDate());
			user.setGioiTinh(gioiTinh); // Cập nhật giới tính
			userService.updateUser(user);

			model.addAttribute("message", "Cập nhật thông tin cá nhân thành công!");
		} catch (Exception e) {
			model.addAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin cá nhân.");
		}
		return "redirect:/CaiDat";
	}
}
