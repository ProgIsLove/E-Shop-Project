package com.example.shopmebe.controller;

import com.example.shopmebe.service.ProductService;
import com.shopme.common.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.listAll();

        model.addAttribute("products", products);

        return "products/products";
    }
}
