package com.example.trainup.dto.users;

import com.example.trainup.validation.RegexConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequestDto(
        @NotBlank
        @Email(message = "Email is not valid",
                regexp = RegexConstants.EMAIL_REGEX)
        String email,
        @NotBlank
        @Pattern(regexp = RegexConstants.PASSWORD_REGEX)
        String password
) {
}
