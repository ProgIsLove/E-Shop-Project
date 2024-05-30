package com.example.shopmebe.rest;

import com.example.shopmebe.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BrandRestController {

    private final BrandService brandService;

    @PostMapping("/v1/brands/check-unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return brandService.checkUnique(id, name);
    }
}
