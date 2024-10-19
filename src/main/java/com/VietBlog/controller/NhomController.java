package com.VietBlog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhomController {

    @GetMapping("/Nhom")
    public String Nhom(){
        return "Nhom";
    }
}
