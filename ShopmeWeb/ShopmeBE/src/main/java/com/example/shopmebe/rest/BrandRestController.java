package com.example.shopmebe.rest;

import com.example.shopmebe.dto.CategoryDTO;
import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.exception.BrandNotFoundRestException;
import com.example.shopmebe.service.BrandService;
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
@RequestMapping("/v1/brands")
public class BrandRestController {

    private final BrandService brandService;

    @PostMapping("/check-unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();

        try {
            Brand brandById = brandService.getBrandById(brandId);
            Set<Category> categories = brandById.getCategories();

            for (Category category : categories) {
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(categoryDTO);
            }

            return listCategories;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
