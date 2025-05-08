package com.example.trainup.service;

import com.example.trainup.dto.users.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.TrainerResponseDto;

public interface TrainerService {
    TrainerResponseDto register(TrainerRegistrationRequestDto requestDto);
}
