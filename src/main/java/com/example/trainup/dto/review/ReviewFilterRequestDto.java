package com.example.trainup.dto.review;

public record ReviewFilterRequestDto(
        Long id,
        int rating,
        Long authorId,
        Long gymId,
        Long trainerId
) {
}
