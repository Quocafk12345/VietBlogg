package com.VietBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/nhom")
public class NhomController_new {

    @GetMapping("/tao-nhom")
    public String hienThiTrangTaoNhom() {
        return "TaoNhom";
    }
}