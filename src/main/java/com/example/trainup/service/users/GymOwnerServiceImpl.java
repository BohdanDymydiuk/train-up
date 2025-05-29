package com.example.trainup.service.users;

import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;
import com.example.trainup.mapper.GymOwnerMapper;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymOwnerServiceImpl implements GymOwnerService {
    private final GymOwnerRepository gymOwnerRepository;
    private final GymOwnerMapper gymOwnerMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialService userCredentialService;

    @Override
    public GymOwnerResponseDto register(GymOwnerRegistrationRequestDto requestDto) {
        GymOwner gymOwner = gymOwnerMapper.toModel(requestDto, passwordEncoder);
        gymOwnerRepository.save(gymOwner);
        userCredentialService.assignRoleBasedOnUserType(gymOwner.getUserCredentials());
        return gymOwnerMapper.toDto(gymOwner);
    }
}
