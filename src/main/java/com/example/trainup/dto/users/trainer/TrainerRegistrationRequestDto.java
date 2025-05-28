package com.example.trainup.dto.users.trainer;

import com.example.trainup.model.enums.Gender;
import com.example.trainup.validation.EmailUnique;
import com.example.trainup.validation.RegexConstants;
import com.example.trainup.validation.ValidPhoneNumbers;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record TrainerRegistrationRequestDto(
        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,

        Gender gender,

        LocalDate dateOfBirth,

        String profileImageUrl,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email is not valid",
                regexp = RegexConstants.EMAIL_REGEX)
        @EmailUnique
        String email,

        @NotBlank
        @Pattern(regexp = RegexConstants.PASSWORD_REGEX)
        String password,

        @NotBlank
        String repeatPassword,

        @NotEmpty(message = "Enter your phone number")
        @ValidPhoneNumbers
        Set<String> phoneNumbers,

        @NotEmpty(message = "Sport Ids cannot be empty")
        Set<Long> sportIds,

        Set<Long> gymIds,

        TrainerAddressDto location,

        Boolean onlineTraining,

        List<String> certificates,

        String description,

        String socialMediaLinks
) {
}
