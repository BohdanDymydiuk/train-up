package com.example.trainup.controller;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.service.ReviewService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@Validated
@RequiredArgsConstructor
@Log4j2
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewResponseDto> getAllReview(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long gymId,
            @RequestParam(required = false) Long trainerId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        ReviewFilterRequestDto filter = new ReviewFilterRequestDto(
                id,
                rating,
                authorId,
                gymId,
                trainerId
        );
        List<ReviewResponseDto> responseDtos = reviewService.getAllReviews(filter, pageable);
        return responseDtos;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReviewById(@PathVariable @Positive Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/gym/{gymId}")
    @PreAuthorize("hasRole('ATHLETE')")
    public ReviewResponseDto createGymReview(
            @PathVariable @Positive Long gymId,
            @RequestBody ReviewRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        ReviewResponseDto responseDto = reviewService
                .createGymReview(authentication, gymId, requestDto);
        return responseDto;
    }

    @PostMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('ATHLETE')")
    public ReviewResponseDto createTrainerReview(
            @PathVariable @Positive Long trainerId,
            @RequestBody ReviewRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        ReviewResponseDto responseDto = reviewService
                .createTrainerReview(authentication, trainerId, requestDto);
        return responseDto;
    }
}
