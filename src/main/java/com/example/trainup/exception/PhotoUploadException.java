package com.example.trainup.exception;

public class PhotoUploadException extends RuntimeException {
    public PhotoUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
