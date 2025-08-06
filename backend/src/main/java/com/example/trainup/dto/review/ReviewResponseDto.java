package com.example.trainup.dto.review;

public record ReviewResponseDto(
        Long id,
        Integer rating,
        String description,
        Long gymId,
        Long trainerId,
        Long authorId
) {
}
