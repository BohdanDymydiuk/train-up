package com.example.trainup.service;

import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;

public interface AthleteService {
    AthleteResponseDto register(AthleteRegistrationRequestDto requestDto);
}
