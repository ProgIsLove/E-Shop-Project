package com.example.shopmebe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
public enum BusinessErrorCodes {

    DUPLICATE_ENTRY(409, CONFLICT, "Duplicate entry"),
    ;

    private final int code;
    private final String title;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String title) {
        this.code = code;
        this.title = title;
        this.httpStatus = status;
    }
}
