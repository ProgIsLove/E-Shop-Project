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

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("categories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

}
