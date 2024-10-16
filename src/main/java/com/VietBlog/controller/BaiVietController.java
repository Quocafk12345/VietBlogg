package com.VietBlog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.VietBlog.service.BaiVietService;

@Controller
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/total-bai-viet")
    public String showTotalBaiViet(Model model) {
        int totalBaiViet = baiVietService.getTotalBaiViet();
        model.addAttribute("totalBaiViet", totalBaiViet);
        return "profilepage";  // Tên trang view bạn muốn hiển thị
    }

}

