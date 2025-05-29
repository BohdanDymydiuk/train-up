package com.example.trainup.controller;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@Validated
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "Review Management",
        description = "Endpoints for managing user reviews for gyms and trainers."
)
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    @Operation(
            summary = "Retrieve Reviews by Criteria",
            description = "Retrieves a list of reviews based on various filtering criteria such "
                    + "as ID, rating, author, gym, or trainer."
    )
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
        log.info("Attempting to fetch reviews with filter: {} and pageable: {}", filter, pageable);
        List<ReviewResponseDto> responseDtos = reviewService.getAllReviews(filter, pageable);

        log.info("Successfully retrieved {} reviews with specified criteria.", responseDtos.size());
        return responseDtos;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete Review by ID",
            description = "Allows an ADMIN user to delete a review by its ID."
    )
    public ResponseEntity<Void> deleteReviewById(@PathVariable @Positive Long id) {
        log.info("Attempting to delete review with ID: {}", id);
        reviewService.deleteById(id);

        log.info("Review with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/gym/{gymId}")
    @PreAuthorize("hasRole('ATHLETE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Gym Review",
            description = "Allows an ATHLETE to submit a review for a specific gym."
    )
    public ReviewResponseDto createGymReview(
            @PathVariable @Positive Long gymId,
            @RequestBody ReviewRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Attempting to create gym review for gym ID: {} by athlete '{}' with request: {}",
                gymId, authentication.getName(), requestDto);
        ReviewResponseDto responseDto = reviewService
                .createGymReview(authentication, gymId, requestDto);

        log.info("Gym review successfully created for gym ID: {} by athlete '{}'. Review ID: {}",
                gymId, authentication.getName(), responseDto.id());
        return responseDto;
    }

    @PostMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('ATHLETE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Trainer Review",
            description = "Allows an ATHLETE to submit a review for a specific trainer."
    )
    public ReviewResponseDto createTrainerReview(
            @PathVariable @Positive Long trainerId,
            @RequestBody ReviewRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Attempting to create trainer review for trainer ID: {} by athlete '{}' "
                + "with request: {}", trainerId, authentication.getName(), requestDto);
        ReviewResponseDto responseDto = reviewService
                .createTrainerReview(authentication, trainerId, requestDto);

        log.info("Trainer review successfully created for trainer ID: {} by athlete '{}'. "
                + "Review ID: {}", trainerId, authentication.getName(), responseDto.id());
        return responseDto;
    }
}
