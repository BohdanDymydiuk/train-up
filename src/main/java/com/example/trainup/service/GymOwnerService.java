package com.example.trainup.service;

import com.example.trainup.dto.users.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.GymOwnerResponseDto;

public interface GymOwnerService {
    GymOwnerResponseDto register(GymOwnerRegistrationRequestDto requestDto);
}
