package com.example.trainup.service;

import com.example.trainup.dto.users.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.AthleteResponseDto;

public interface AthleteService {
    AthleteResponseDto register(AthleteRegistrationRequestDto requestDto);
}
