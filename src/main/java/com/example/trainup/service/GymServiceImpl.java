package com.example.trainup.service;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.mapper.GymMapper;
import com.example.trainup.model.Gym;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymServiceImpl implements GymService {
    private final GymRepository gymRepository;
    private final GymMapper gymMapper;
    private final SportRepository sportRepository;
    private final TrainerRepository trainerRepository;
    private final AddressRepository addressRepository;

    @Override
    public GymResponseDto save(GymOwner gymOwner, GymRegistrationRequestDto requestDto) {
        Gym gym = gymMapper.toModel(
                requestDto,
                gymOwner,
                sportRepository,
                trainerRepository,
                addressRepository
        );
        Gym savedGym = gymRepository.save(gym);
        return gymMapper.toDto(savedGym);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymResponseDto> getAllGyms(GymFilterRequestDto filter, Pageable pageable) {
        Page<Gym> gymPage = gymRepository.findGymsByCriteria(
                filter.name(),
                filter.locationCountry(),
                filter.locationCity(),
                filter.locationCityDistrict(),
                filter.locationStreet(),
                filter.locationHouse(),
                filter.sportIds(),
                filter.trainerIds(),
                filter.overallRating(),
                pageable
        );
        return gymPage.stream()
                .map(gymMapper::toDto)
                .toList();
    }
}
