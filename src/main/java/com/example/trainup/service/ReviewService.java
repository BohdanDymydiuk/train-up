package com.example.trainup.service;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    List<ReviewResponseDto> getAllReviews(ReviewFilterRequestDto filter, Pageable pageable);

    void deleteById(Long id);

    ReviewResponseDto createGymReview(Authentication authentication, Long gymId,
                                      ReviewRegistrationRequestDto requestDto);

    ReviewResponseDto createTrainerReview(Authentication authentication, Long trainerId,
                                      ReviewRegistrationRequestDto requestDto);
}
