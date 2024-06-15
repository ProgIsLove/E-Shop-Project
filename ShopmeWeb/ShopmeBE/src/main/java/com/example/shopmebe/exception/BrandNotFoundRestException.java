package com.example.shopmebe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Brand not found")
public class BrandNotFoundRestException extends Exception {
    public BrandNotFoundRestException() {
        super("Brand not found");
    }

    public BrandNotFoundRestException(String message) {
        super(message);
    }
}
