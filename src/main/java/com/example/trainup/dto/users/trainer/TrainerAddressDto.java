package com.example.trainup.dto.users.trainer;

import jakarta.validation.constraints.NotBlank;

public record TrainerAddressDto(
        @NotBlank(message = "Country cannot be blank")
        String country,
        @NotBlank(message = "City cannot be blank")
        String city,
        String cityDistrict,
        String street,
        String house
) {
}
