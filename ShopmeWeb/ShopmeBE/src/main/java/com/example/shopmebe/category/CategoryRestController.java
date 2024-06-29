package com.example.shopmebe.category;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping("/v1/categories/check-unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
        return categoryService.checkUnique(id, name, alias);
    }
}
