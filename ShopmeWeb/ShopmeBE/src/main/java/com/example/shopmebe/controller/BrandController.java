package com.example.shopmebe.controller;


import com.example.shopmebe.service.BrandService;
import com.shopme.common.entity.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public String listAll(Model model) {
        List<Brand> brands = brandService.findAll();
        model.addAttribute("brands", brands);

        return "brands/brands";
    }
}
