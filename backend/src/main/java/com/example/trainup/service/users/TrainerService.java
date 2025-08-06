package com.example.trainup.service.users;

import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface TrainerService {
    TrainerResponseDto register(TrainerRegistrationRequestDto requestDto);

    List<TrainerResponseDto> getAllTrainers(TrainerFilterRequestDto filter, Pageable pageable);

    TrainerResponseDto getTrainerByAuth(Authentication authentication);

    TrainerResponseDto getTrainerById(Long id);

    boolean canUserModifyTrainer(Authentication auth, Long id);

    void deleteTrainerById(Long id);

    TrainerResponseDto updateTrainerByAuth(Authentication authentication,
                                           TrainerUpdateRequestDto requestDto);
}
