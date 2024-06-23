package com.example.shopmebe.controller;

import com.example.shopmebe.service.BrandService;
import com.example.shopmebe.service.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;

    public ProductController(ProductService productService, BrandService brandService) {
        this.productService = productService;
        this.brandService = brandService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.listAll();

        model.addAttribute("products", products);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandService.findAllBrandsWithIdAndName();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("brands", brands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Crete New Product");

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.save(product);

        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully");


        return "redirect:/products";
    }
}
