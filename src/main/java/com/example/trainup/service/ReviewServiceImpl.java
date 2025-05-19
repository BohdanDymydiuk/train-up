package com.example.trainup.service;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.mapper.ReviewMapper;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Rateable;
import com.example.trainup.model.Review;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.ReviewRepository;
import com.example.trainup.repository.TrainerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final AthleteRepository athleteRepository;
    private final GymRepository gymRepository;
    private final TrainerRepository trainerRepository;
    private final AthleteService athleteService;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews(ReviewFilterRequestDto filter, Pageable pageable) {
        log.debug("Fetching reviews with filter: {}", filter);
        Page<Review> reviewPage = reviewRepository.findReviewsByCriteria(
                filter.id(),
                filter.rating(),
                filter.authorId(),
                filter.gymId(),
                filter.trainerId(),
                pageable
        );

        return reviewPage.stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Review by id:" + id));

        if (review.getGym() != null) {
            Gym gym = gymRepository.findById(review.getGym().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Gym not found"));

            updateRatingOnDelete(gym, review);
            gymRepository.save(gym);
        } else if (review.getTrainer() != null) {
            Trainer trainer = trainerRepository.findById(review.getTrainer().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

            updateRatingOnDelete(trainer, review);
            trainerRepository.save(trainer);
        }

        reviewRepository.deleteById(id);
        log.debug("Review was deleted with ID: {}", id);
    }

    @Override
    public ReviewResponseDto createGymReview(
            Authentication authentication,
            Long gymId,
            ReviewRegistrationRequestDto requestDto
    ) {
        Athlete athlete = getAthleteFromAuthentication(authentication);

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Gym not found with id: " + gymId));

        Review review = reviewMapper.toModelWithGym(requestDto, athlete, gym);
        Review savedReview = reviewRepository.save(review);

        updateRatingOnAdd(gym, savedReview);
        gymRepository.save(gym);

        log.debug("Created review for Gym ID: {} by Athlete ID: {}", gymId, athlete.getId());
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewResponseDto createTrainerReview(
            Authentication authentication,
            Long trainerId,
            ReviewRegistrationRequestDto requestDto
    ) {
        Athlete athlete = getAthleteFromAuthentication(authentication);
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with id: "
                        + trainerId));

        Review review = reviewMapper.toModelWithTrainer(requestDto, athlete, trainer);
        Review savedReview = reviewRepository.save(review);

        updateRatingOnAdd(trainer, savedReview);
        trainerRepository.save(trainer);

        log.debug("Created review for Trainer ID: {} by Athlete ID: {}",
                trainerId, athlete.getId());
        return reviewMapper.toDto(savedReview);
    }

    private void updateRatingOnAdd(Rateable entity, Review review) {
        if (entity.getReviews() == null) {
            entity.setReviews(new ArrayList<>());
        }
        entity.getReviews().add(review);

        int newReviewCount = entity.getNumberOfReviews() + 1;
        float currentTotalRating = entity.getOverallRating() * entity.getNumberOfReviews();
        float newTotalRating = currentTotalRating + review.getRating();
        float updatedRating = newReviewCount > 0 ? newTotalRating / newReviewCount : 0.0f;

        entity.setOverallRating(updatedRating);
        entity.setNumberOfReviews(newReviewCount);
    }

    private void updateRatingOnDelete(Rateable entity, Review review) {
        if (entity.getReviews() == null) {
            entity.setReviews(new ArrayList<>());
        }
        entity.getReviews().remove(review);

        int newReviewCount = entity.getNumberOfReviews() - 1;
        float currentTotalRating = entity.getOverallRating() * entity.getNumberOfReviews();
        float newTotalRating = currentTotalRating - review.getRating();
        float updatedRating = newReviewCount > 0 ? newTotalRating / newReviewCount : 0.0f;

        entity.setOverallRating(updatedRating);
        entity.setNumberOfReviews(newReviewCount);
    }

    private Athlete getAthleteFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        String email = authentication.getName();
        return athleteRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can not find current user with email: " + email));
    }
}
