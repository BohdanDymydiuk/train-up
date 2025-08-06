package com.example.trainup.model.enums;

public enum Gender {
    MALE('M'),
    FEMALE('F');

    private final char code;

    Gender(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }
}
