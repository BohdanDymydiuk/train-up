package com.example.trainup.dto.review;

public record ReviewFilterRequestDto(
        Long id,
        Integer rating,
        Long authorId,
        Long gymId,
        Long trainerId
) {
}
