package com.example.shopmebe.category;

import com.shopme.common.request.CheckUniqueNameWithAliasRequest;
import com.shopme.common.response.CheckUniqueResponse;
import com.shopme.common.response.CheckUniqueStatus;
import com.example.shopmebe.exception.ConflictException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping("/check-unique")
    public ResponseEntity<CheckUniqueResponse> checkUnique(@RequestBody CheckUniqueNameWithAliasRequest request) throws ConflictException {
        categoryService.checkUnique(request);

        return ResponseEntity.ok(new CheckUniqueResponse(CheckUniqueStatus.OK));
    }
}
