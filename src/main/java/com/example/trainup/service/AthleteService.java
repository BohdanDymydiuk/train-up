package com.example.trainup.service;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface AthleteService {
    AthleteResponseDto register(AthleteRegistrationRequestDto requestDto);

    AthleteResponseDto getAthleteById(Long id);

    boolean canUserModifyAthlete(Authentication authentication,Long id);

    List<AthleteResponseDto> getAllAthlete(AthleteFilterRequestDto filter, Pageable pageable);

    AthleteResponseDto updateAthleteByAuth(Authentication auth,
                                           AthleteUpdateRequestDto requestDto);

    void deleteAthleteById(Long id);
}
