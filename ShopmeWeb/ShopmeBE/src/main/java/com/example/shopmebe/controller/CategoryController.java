package com.example.shopmebe.controller;

import com.example.shopmebe.exception.CategoryNotFoundException;
import com.example.shopmebe.export.CategoryCsvExporter;
import com.example.shopmebe.service.CategoryService;
import com.example.shopmebe.utils.CategoryPageInfo;
import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
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
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        CategoryPageInfo page = new CategoryPageInfo();
        List<Category> listCategories = categoryService.listByPage(page, pageNum, sortDir, keyword);

        long startCount = (long) (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortField", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

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

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        return "redirect:/categories";
    }

    @GetMapping("categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        try {
            Category category = categoryService.categoryById(id);
            List<Category> categories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", """
                              Edit Category (ID: %s)
                              """.formatted(id));
            return "categories/category_form";
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = String.format("The category ID %d has been %s", id, status);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            String categoryDir = "../category-images" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message",
                    String.format("The category ID %s has been deleted successfully", id));
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategories, response);
    }
}
