package com.example.shopmebe.controller;


import com.example.shopmebe.service.BrandService;
import com.example.shopmebe.service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class BrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/brands")
    public String listAll(Model model) {
        List<Brand> brands = brandService.listBrands();
        model.addAttribute("brands", brands);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("brand", new Brand());
        model.addAttribute("categories", listCategories);
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }
}
