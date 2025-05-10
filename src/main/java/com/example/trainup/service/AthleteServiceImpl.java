package com.example.trainup.service;

import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.mapper.AthleteMapper;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AthleteServiceImpl implements AthleteService {
    private final AthleteMapper athleteMapper;
    private final PasswordEncoder passwordEncoder;
    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;

    @Override
    public AthleteResponseDto register(AthleteRegistrationRequestDto requestDto) {
        Athlete athlete = athleteMapper.toModel(requestDto, passwordEncoder, sportRepository);
        athleteRepository.save(athlete);
        return athleteMapper.toDto(athlete);
    }
}
