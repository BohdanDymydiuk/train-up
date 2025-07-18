package com.example.trainup.exception;

public class ReflectionAccessException extends RuntimeException {
    public ReflectionAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionAccessException(String message) {
        super(message);
    }
}
