package com.example.trainup.dto.users.trainer;

import java.time.LocalDate;
import java.util.Set;

public record TrainerResponseDto(
        Long id,
        String firstName,
        String lastName,
        Character maleOrFemale,
        LocalDate dateOfBirth,
        String profileImageUrl,
        String email,
        String userType,
        Set<String> phoneNumbers,
        Set<Long> sportIds,
        Set<Long> gymIds,
        TrainerAddressDto location,
        String description,
        String socialMediaLinks,
        Float overallRating,
        Integer numberOfReviews
) {
}
