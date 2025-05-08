package com.example.trainup.service;

import com.example.trainup.dto.users.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.TrainerResponseDto;
import com.example.trainup.mapper.TrainerMapper;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public TrainerResponseDto register(TrainerRegistrationRequestDto requestDto) {
        Trainer trainer = trainerMapper.toModel(requestDto);
        trainerRepository.save(trainer);
        trainer.getUserCredentials().setUserId(trainer.getId());
        trainerRepository.save(trainer);
        return trainerMapper.toDto(trainer);

    }
}
