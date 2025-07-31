package com.example.trainup.service;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface SportService {
    List<SportDto> getAllSports(SportDto sportFilterDto, Pageable pageable);

    SportDto createSport(SportRequestDto requestDto);

    SportDto updateSport(Long id, SportRequestDto requestDto);

    void deleteSport(Long id);
}
