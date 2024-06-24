package com.example.shopmebe.exception.error;

import lombok.Getter;

@Getter
public enum CustomStatusCode {

    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Sorry, you don't have permission to access this page"),
    NOT_FOUND(404, "Sorry, the requested page could not be found"),
    INTERNAL_ERROR(500, "Sorry, the server has encountered an error while processing your request");

    private final int errorCode;
    private final String msg;

    CustomStatusCode(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
