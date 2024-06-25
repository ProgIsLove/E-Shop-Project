package com.example.shopmebe.rest;

import com.example.shopmebe.dto.CategoryDTO;
import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.exception.BrandNotFoundRestException;
import com.example.shopmebe.service.BrandService;
import com.example.shopmebe.service.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/products")
public class ProductRestController {

    private final ProductService productService;

    @PostMapping("/check-unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return productService.checkUnique(id, name);
    }
}
