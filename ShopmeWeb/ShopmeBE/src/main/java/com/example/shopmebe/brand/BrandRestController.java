package com.example.shopmebe.brand;


import com.example.shopmebe.exception.BrandNotFoundException;
import com.shopme.common.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/brands")
public class BrandRestController {

    private final BrandService brandService;

    @PostMapping(value = "/check-unique", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckUniqueResponse> checkUnique(@RequestBody CheckUniqueRequest request) {
        BrandStatus status = brandService.checkUnique(request);

        return switch (status) {
            case OK -> ResponseEntity.ok(new CheckUniqueResponse(BrandStatus.OK));
            case DUPLICATE_NAME ->
                    ResponseEntity.status(CONFLICT).body(new CheckUniqueResponse(BrandStatus.DUPLICATE_NAME));
        };
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
