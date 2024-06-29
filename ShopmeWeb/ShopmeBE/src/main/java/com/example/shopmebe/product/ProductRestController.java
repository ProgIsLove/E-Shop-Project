package com.example.shopmebe.product;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

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
