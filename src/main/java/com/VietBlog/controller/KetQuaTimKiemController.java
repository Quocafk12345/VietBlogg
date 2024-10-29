package com.VietBlog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KetQuaTimKiemController {
    @GetMapping("/Ket_qua_tim_kiem")
    public String KetQuaTimKiem() {
        return "page/KetQuaTimKiem";
    }

}
