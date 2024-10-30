package com.example.shopmebe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.example.shopmebe.exception.BusinessErrorCodes.DUPLICATE_ENTRY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiError> handleBrandNotFoundException(BrandNotFoundException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ApiError.builder()
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
                .body(ApiError.builder()
                                .title(DUPLICATE_ENTRY.getTitle())
                                .code(DUPLICATE_ENTRY.getCode())
                                .details(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ApiError> handleCountryNotFoundException(CountryNotFoundException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ApiError.builder()
                                .title(NOT_FOUND.name())
                                .code(HttpStatus.NOT_FOUND.value())
                                .details(e.getMessage())
                                .build()
                );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
