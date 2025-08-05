package com.example.trainup.mapper;

import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Review;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.Trainer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-05T16:27:01+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toModelWithGym(ReviewRegistrationRequestDto requestDto, Athlete author, Gym gym) {
        if ( requestDto == null && author == null && gym == null ) {
            return null;
        }

        Review review = new Review();

        if ( requestDto != null ) {
            review.setRating( requestDto.rating() );
            review.setDescription( requestDto.description() );
        }
        review.setAuthor( author );
        review.setGym( gym );

        return review;
    }

    @Override
    public Review toModelWithTrainer(ReviewRegistrationRequestDto requestDto, Athlete author, Trainer trainer) {
        if ( requestDto == null && author == null && trainer == null ) {
            return null;
        }

        Review review = new Review();

        if ( requestDto != null ) {
            review.setRating( requestDto.rating() );
            review.setDescription( requestDto.description() );
        }
        review.setAuthor( author );
        review.setTrainer( trainer );

        return review;
    }

    @Override
    public ReviewResponseDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        Long id = null;
        Integer rating = null;
        String description = null;

        id = review.getId();
        rating = review.getRating();
        description = review.getDescription();

        Long authorId = review.getAuthor() != null ? review.getAuthor().getId() : null;
        Long gymId = review.getGym() != null ? review.getGym().getId() : null;
        Long trainerId = review.getTrainer() != null ? review.getTrainer().getId() : null;

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto( id, rating, description, gymId, trainerId, authorId );

        return reviewResponseDto;
    }
}
