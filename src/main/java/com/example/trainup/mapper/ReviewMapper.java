package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Review;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "rating", source = "requestDto.rating")
    @Mapping(target = "description", source = "requestDto.description")
    Review toModelWithGym(ReviewRegistrationRequestDto requestDto, Athlete author, Gym gym);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gym", ignore = true)
    @Mapping(target = "rating", source = "requestDto.rating")
    @Mapping(target = "description", source = "requestDto.description")
    Review toModelWithTrainer(ReviewRegistrationRequestDto requestDto,
                              Athlete author, Trainer trainer);

    @Mapping(target = "authorId", expression = "java(review.getAuthor() != null ? "
            + "review.getAuthor().getId() : null)")
    @Mapping(target = "gymId", expression = "java(review.getGym() != null ? "
            + "review.getGym().getId() : null)")
    @Mapping(target = "trainerId", expression = "java(review.getTrainer() != null ? "
            + "review.getTrainer().getId() : null)")
    ReviewResponseDto toDto(Review review);
}
