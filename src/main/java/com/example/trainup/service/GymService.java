package com.example.trainup.service;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.model.user.GymOwner;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface GymService {
    GymResponseDto save(GymOwner gymOwner, GymRegistrationRequestDto requestDto);

    List<GymResponseDto> getAllGyms(GymFilterRequestDto filter, Pageable pageable);

    Page<GymResponseDto> getGymsByGymOwner(Authentication authentication, Pageable pageable);

    GymResponseDto getGymById(Long id);

    void deleteGymById(Long id);

    boolean canUserModifyGym(Authentication authentication, Long gymId);

    GymResponseDto updateGym(Long id, GymUpdateRequestDto requestDto);

    String uploadGymPhoto(Long id, MultipartFile file, Authentication authentication);
}
