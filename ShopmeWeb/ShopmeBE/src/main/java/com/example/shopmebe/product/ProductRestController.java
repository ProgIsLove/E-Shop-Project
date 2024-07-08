package com.example.shopmebe.product;

import com.shopme.common.response.CheckUniqueStatus;
import com.shopme.common.request.CheckUniqueNameRequest;
import com.shopme.common.response.CheckUniqueResponse;
import com.example.shopmebe.exception.ConflictException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/products")
public class ProductRestController {

    private final ProductService productService;

    @PostMapping(value = "/check-unique", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckUniqueResponse> checkUnique(@RequestBody CheckUniqueNameRequest request) throws ConflictException {
        productService.checkUnique(request);

        return ResponseEntity.ok(new CheckUniqueResponse(CheckUniqueStatus.OK));
    }
}
