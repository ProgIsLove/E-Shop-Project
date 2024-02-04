package com.example.shopmebe.controller;

import com.example.shopmebe.service.CategoryService;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model) {
        List<Category> listCategories = categoryService.listALl();
        model.addAttribute("listCategories", listCategories);

        return "categories/categories";
    }


}
