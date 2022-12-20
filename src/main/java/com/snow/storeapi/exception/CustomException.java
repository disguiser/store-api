package com.snow.storeapi.exception;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 8796850459645546215L;

    public CustomException(String message) {
        super(message);
    }
}