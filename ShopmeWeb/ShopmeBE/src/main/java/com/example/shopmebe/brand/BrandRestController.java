package com.example.shopmebe.brand;


import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.request.CheckUniqueNameRequest;
import com.shopme.common.response.CheckUniqueResponse;
import com.shopme.common.response.CheckUniqueStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/brands")
public class BrandRestController {

    private final BrandService brandService;

    @PostMapping(value = "/check-unique", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckUniqueResponse> checkUnique(@RequestBody CheckUniqueNameRequest request) throws ConflictException {
        brandService.checkUnique(request);

        return ResponseEntity.ok(new CheckUniqueResponse(CheckUniqueStatus.OK));
    }

    @GetMapping(value = "/{id}/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundException {
            Brand brandById = brandService.getBrandById(brandId);
            Set<Category> categories = brandById.getCategories();

            List<CategoryDTO> categoriesDTO = categories.stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName()))
                    .toList();

            return ResponseEntity.ok(categoriesDTO);
    }
}
