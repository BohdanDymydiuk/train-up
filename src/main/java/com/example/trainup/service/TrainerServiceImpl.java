package com.example.trainup.service;

import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.mapper.TrainerMapper;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final PasswordEncoder encoder;
    private final SportRepository sportRepository;
    private final GymRepository gymRepository;
    private final AddressRepository addressRepository;

    @Override
    public TrainerResponseDto register(TrainerRegistrationRequestDto requestDto) {
        Trainer trainer = trainerMapper
                .toModel(requestDto, encoder, sportRepository, gymRepository, addressRepository);
        trainerRepository.save(trainer);
        return trainerMapper.toDto(trainer);

    }
}
