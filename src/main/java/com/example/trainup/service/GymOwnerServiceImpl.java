package com.example.trainup.service;

import com.example.trainup.dto.users.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.GymOwnerResponseDto;
import com.example.trainup.mapper.GymOwnerMapper;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.repository.GymOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymOwnerServiceImpl implements GymOwnerService {
    private final GymOwnerRepository gymOwnerRepository;
    private final GymOwnerMapper gymOwnerMapper;

    @Override
    public GymOwnerResponseDto register(GymOwnerRegistrationRequestDto requestDto) {
        GymOwner gymOwner = gymOwnerMapper.toModel(requestDto);
        gymOwnerRepository.save(gymOwner);
        gymOwner.getUserCredentials().setUserId(gymOwner.getId());
        gymOwnerRepository.save(gymOwner);
        return gymOwnerMapper.toDto(gymOwner);
    }
}
