package com.example.shopmefe.product;

import com.example.shopmefe.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                     Model model) {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                               @PathVariable("pageNum") int pageNum,
                               Model model) {
        Category category = categoryService.getCategory(alias);

        List<Category> categoryParents = categoryService.getCategoryParents(category);
        Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
        List<Product> products = pageProducts.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());

        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("categoryParents", categoryParents);
        model.addAttribute("products", products);
        model.addAttribute("category", category);

        return "category/products_by_category";
    }
}
