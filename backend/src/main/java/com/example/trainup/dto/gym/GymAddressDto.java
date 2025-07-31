package com.example.trainup.dto.gym;

import jakarta.validation.constraints.NotBlank;

public record GymAddressDto(
        @NotBlank(message = "Country cannot be blank")
        String country,

        @NotBlank(message = "City cannot be blank")
        String city,

        String cityDistrict,

        @NotBlank(message = "Street cannot be blank")
        String street,

        @NotBlank(message = "House cannot be blank")
        String house
) {
}
