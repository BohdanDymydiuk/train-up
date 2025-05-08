package com.example.trainup.dto.users;

import com.example.trainup.validation.EmailUnique;
import com.example.trainup.validation.RegexConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

public record GymOwnerRegistrationRequestDto(
        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,

        Character maleOrFemale,

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
        @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX)
        Set<String> phoneNumbers
) {
}
