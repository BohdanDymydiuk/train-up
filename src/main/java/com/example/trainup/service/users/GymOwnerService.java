package com.example.trainup.service.users;

import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;

public interface GymOwnerService {
    GymOwnerResponseDto register(GymOwnerRegistrationRequestDto requestDto);
}
