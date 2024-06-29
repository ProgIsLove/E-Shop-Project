package com.example.shopmebe.exception.error;

import java.util.Map;
import java.util.NoSuchElementException;

public class CustomStatusCodeCommand {

    private static final Map<Integer, CustomStatusCode> STATUS_CODE;

    static {

        STATUS_CODE = Map.of(
                403, CustomStatusCode.FORBIDDEN,
                404, CustomStatusCode.NOT_FOUND,
                415, CustomStatusCode.MEDIA_TYPE_UNSUPPORTED,
                500, CustomStatusCode.INTERNAL_ERROR);
    }

    public CustomStatusCode createStatusCode(Integer statusCode) {
        CustomStatusCode customStatusCode = STATUS_CODE.get(statusCode);

        if (customStatusCode == null) {
            throw new NoSuchElementException(String.format("Invalid status code type: %d", statusCode));
        }

        return customStatusCode;
    }
}
