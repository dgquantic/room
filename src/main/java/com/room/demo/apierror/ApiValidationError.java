package com.room.demo.apierror;

public record ApiValidationError(String object, String field, Object rejectedValue, String message) {
    public ApiValidationError (String object, String message) {
        this(object, null, null, message);
    }
}
