package com.example.shopmebe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.shopmebe.exception.BusinessErrorCodes.DUPLICATE_ENTRY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiError> handleBrandNotFoundException(BrandNotFoundException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ApiError.builder()
                                .title(NOT_FOUND.name())
                                .code(HttpStatus.NOT_FOUND.value())
                                .details(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException e) {
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ApiError.builder()
                                .title(DUPLICATE_ENTRY.getTitle())
                                .code(DUPLICATE_ENTRY.getCode())
                                .details(e.getMessage())
                                .build()
                );
    }
}
