package com.example.shopmebe.controller;


import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.exception.CategoryNotFoundException;
import com.example.shopmebe.service.BrandService;
import com.example.shopmebe.service.CategoryService;
import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/brands/save")
    public String saveCategory(Brand brand,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            brand.setLogo(fileName);

            Brand savedCategory = brandService.save(brand);
            String uploadDir = "../brand-images/" + savedCategory.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
        return "redirect:/brands";
    }

    @GetMapping("brands/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        try {
            Brand brand = brandService.getBrandById(id);
            List<Category> categories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", """
                              Edit Brand (ID: %s)
                              """.formatted(id));
            return "brands/brand_form";
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            String categoryDir = "../brand-images" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message",
                    String.format("The brand ID %s has been deleted successfully", id));
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/brands";
    }
}
