package com.example.shopmefe.customer;

import com.example.shopmefe.exception.ConflictException;
import com.shopme.common.request.CheckUniqueEmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/v1")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers/check-email")
    public ResponseEntity<?> checkDuplicateEmail(@RequestBody CheckUniqueEmailRequest request) {
        try {
            boolean emailUnique = customerService.isEmailUnique(request.email());
            return ResponseEntity.ok(emailUnique);
        } catch (ConflictException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
