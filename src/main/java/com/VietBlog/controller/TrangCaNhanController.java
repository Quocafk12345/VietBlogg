package com.VietBlog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrangCaNhanController {
    @GetMapping("/Trang_ca_nhan")
    public static String trangCaNhan() { return "account/trangCaNhan"; }
}
