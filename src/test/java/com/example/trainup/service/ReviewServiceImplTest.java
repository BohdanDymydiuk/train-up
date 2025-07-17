package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.mapper.ReviewMapper;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Review;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.ReviewRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.service.users.AthleteService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private AthleteService athleteService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Athlete athlete;
    private Gym gym;
    private Trainer trainer;
    private Review review;
    private ReviewFilterRequestDto filterRequestDto;
    private ReviewRegistrationRequestDto registrationRequestDto;
    private ReviewResponseDto responseDto;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        athlete = new Athlete();
        athlete.setId(1L);
        athlete.setUserCredentials(new UserCredentials());
        athlete.getUserCredentials().setEmail("athlete@example.com");

        gym = new Gym();
        gym.setId(1L);
        gym.setOverallRating(0.0f);
        gym.setNumberOfReviews(0);
        gym.setReviews(new java.util.ArrayList<>());

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setOverallRating(0.0f);
        trainer.setNumberOfReviews(0);
        trainer.setReviews(new java.util.ArrayList<>());

        review = new Review();
        review.setId(1L);
        review.setRating(4);
        review.setDescription("Great service!");
        review.setAuthor(athlete);
        review.setGym(gym);

        filterRequestDto = new ReviewFilterRequestDto(null, 0, null, null, null);

        registrationRequestDto = new ReviewRegistrationRequestDto(4, "Great service!");

        responseDto = new ReviewResponseDto(
                1L,
                4,
                "Great service!",
                1L,
                null,
                1L
        );

        authentication = new UsernamePasswordAuthenticationToken("athlete@example.com", null);
    }

    @Test
    void getAllReviews_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(review));
        when(reviewRepository.findReviewsByCriteria(
                eq(filterRequestDto.id()),
                eq(filterRequestDto.rating()),
                eq(filterRequestDto.authorId()),
                eq(filterRequestDto.gymId()),
                eq(filterRequestDto.trainerId()),
                eq(pageable)
        )).thenReturn(reviewPage);
        when(reviewMapper.toDto(review)).thenReturn(responseDto);

        // When
        List<ReviewResponseDto> result = reviewService.getAllReviews(filterRequestDto, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(reviewRepository).findReviewsByCriteria(
                eq(filterRequestDto.id()),
                eq(filterRequestDto.rating()),
                eq(filterRequestDto.authorId()),
                eq(filterRequestDto.gymId()),
                eq(filterRequestDto.trainerId()),
                eq(pageable)
        );
        verify(reviewMapper).toDto(review);
    }

    @Test
    void deleteById_SuccessWithGym() {
        // Given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // When
        reviewService.deleteById(1L);

        // Then
        assertEquals(0, gym.getNumberOfReviews());
        assertEquals(0.0f, gym.getOverallRating());
        verify(reviewRepository).findById(1L);
        verify(gymRepository).findById(1L);
        verify(gymRepository).save(gym);
        verify(reviewRepository).deleteById(1L);
    }

    @Test
    void deleteById_SuccessWithTrainer() {
        // Given
        review.setGym(null);
        review.setTrainer(trainer);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        // When
        reviewService.deleteById(1L);

        // Then
        assertEquals(0, trainer.getNumberOfReviews());
        assertEquals(0.0f, trainer.getOverallRating());
        verify(reviewRepository).findById(1L);
        verify(trainerRepository).findById(1L);
        verify(trainerRepository).save(trainer);
        verify(reviewRepository).deleteById(1L);
    }

    @Test
    void createGymReview_Success() {
        // Given
        when(athleteRepository.findByEmail("athlete@example.com"))
                .thenReturn(Optional.of(athlete));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
        when(reviewMapper.toModelWithGym(registrationRequestDto, athlete, gym)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(responseDto);

        // When
        ReviewResponseDto result = reviewService.createGymReview(
                authentication,
                1L,
                registrationRequestDto
        );

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        assertEquals(1, gym.getNumberOfReviews());
        assertEquals(4.0f, gym.getOverallRating());
        verify(athleteRepository).findByEmail("athlete@example.com");
        verify(gymRepository).findById(1L);
        verify(reviewMapper).toModelWithGym(registrationRequestDto, athlete, gym);
        verify(reviewRepository).save(review);
        verify(gymRepository).save(gym);
        verify(reviewMapper).toDto(review);
    }

    @Test
    void createTrainerReview_Success() {
        // Given
        when(athleteRepository.findByEmail("athlete@example.com")).thenReturn(Optional.of(athlete));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(reviewMapper.toModelWithTrainer(registrationRequestDto, athlete, trainer))
                .thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(responseDto);

        // When
        ReviewResponseDto result = reviewService.createTrainerReview(
                authentication,
                1L,
                registrationRequestDto
        );

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        assertEquals(1, trainer.getNumberOfReviews());
        assertEquals(4.0f, trainer.getOverallRating());
        verify(athleteRepository).findByEmail("athlete@example.com");
        verify(trainerRepository).findById(1L);
        verify(reviewMapper).toModelWithTrainer(registrationRequestDto, athlete, trainer);
        verify(reviewRepository).save(review);
        verify(trainerRepository).save(trainer);
        verify(reviewMapper).toDto(review);
    }
}
