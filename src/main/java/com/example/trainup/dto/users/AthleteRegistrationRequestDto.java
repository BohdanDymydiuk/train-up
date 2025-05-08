package com.example.trainup.dto.users;

import com.example.trainup.validation.EmailUnique;
import com.example.trainup.validation.FieldMatch;
import com.example.trainup.validation.RegexConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@FieldMatch(
        firstString = "password",
        secondString = "repeatPassword",
        message = "The password fields must match"
)
public record AthleteRegistrationRequestDto(
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
        Set<String> phoneNumbers,

        Set<Long> sportIds,

        @NotNull(message = "Would you like to receive email updates about upcoming events, "
                            + "open workouts, competitions, and promotional offers?")
        boolean emailPermission,

        @NotNull(message = "Would you like to receive SMS updates about upcoming events, "
                + "open workouts, competitions, and promotional offers?")
        boolean phonePermission
        ) {
}
