package com.example.trainup.dto.sport;

import jakarta.validation.constraints.NotEmpty;

public record SportRequestDto(
        @NotEmpty
        String sportName) {
}
