package com.example.trainup.service;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.model.user.GymOwner;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GymService {
    GymResponseDto save(GymOwner gymOwner, GymRegistrationRequestDto requestDto);

    List<GymResponseDto> getAllGyms(GymFilterRequestDto filter, Pageable pageable);

    Page<GymResponseDto> getGymsByGymOwner(GymOwner gymOwner, Pageable pageable);

    GymResponseDto getGymById(Long id);

    void deleteGymById(Long id);

    boolean canUserModifyGym(String email, Long gymId);

    GymResponseDto updateGym(Long id, GymUpdateRequestDto requestDto);
}
