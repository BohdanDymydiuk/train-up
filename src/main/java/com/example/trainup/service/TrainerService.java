package com.example.trainup.service;

import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;

public interface TrainerService {
    TrainerResponseDto register(TrainerRegistrationRequestDto requestDto);
}
