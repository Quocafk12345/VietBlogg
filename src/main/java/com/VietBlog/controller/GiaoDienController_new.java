package com.VietBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GiaoDienController_new {
    @GetMapping("/nhom/chi-tiet/{idNhom}")
    public String chiTietNhom(@PathVariable Long idNhom, Model model) {
        model.addAttribute("idNhom", idNhom);
        return "ChiTietNhom";
    }
}
