package com.example.trainup.service;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.mapper.SportMapper;
import com.example.trainup.model.Sport;
import com.example.trainup.repository.SportRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class SportServiceImpl implements SportService {
    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SportDto> getAllSports(SportDto sportFilterDto, Pageable pageable) {
        Page<Sport> sportPage = sportRepository.findSportsByCriteria(
                sportFilterDto.id(),
                sportFilterDto.sportName(),
                pageable
        );

        return sportPage.stream()
                .map(sportMapper::toDto)
                .toList();
    }

    @Override
    public SportDto createSport(SportRequestDto requestDto) {
        Sport sport = sportMapper.toModel(requestDto);
        Sport savedSport = sportRepository.save(sport);
        return sportMapper.toDto(savedSport);
    }

    @Override
    public SportDto updateSport(Long id, SportRequestDto requestDto) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Sport by id: " + id));
        if (sportRepository.findBySportName((requestDto.sportName())).isPresent()
                && !sport.getSportName().equals(requestDto.sportName())) {
            throw new IllegalArgumentException("Sport with name " + requestDto.sportName()
                    + " already exists");
        }

        sport.setSportName(requestDto.sportName());
        Sport savedSport = sportRepository.save(sport);
        return sportMapper.toDto(savedSport);
    }

    @Override
    public void deleteSport(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Sport by id: " + id));
        sportRepository.deleteById(id);
    }
}
