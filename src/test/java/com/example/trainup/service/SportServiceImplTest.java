package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.mapper.SportMapper;
import com.example.trainup.model.Sport;
import com.example.trainup.repository.SportRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SportServiceImplTest {

    @Mock
    private SportRepository sportRepository;

    @Mock
    private SportMapper sportMapper;

    @InjectMocks
    private SportServiceImpl sportService;

    private Sport sport;
    private SportDto sportDto;
    private SportRequestDto sportRequestDto;

    @BeforeEach
    void setUp() {
        sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Football");

        sportDto = new SportDto(1L, "Football");

        sportRequestDto = new SportRequestDto("Football");
    }

    @Test
    void getAllSports_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sport> sportPage = new PageImpl<>(List.of(sport));
        when(sportRepository.findSportsByCriteria(
                eq(sportDto.id()),
                eq(sportDto.sportName()),
                eq(pageable)
        )).thenReturn(sportPage);
        when(sportMapper.toDto(sport)).thenReturn(sportDto);

        // When
        List<SportDto> result = sportService.getAllSports(sportDto, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sportDto, result.get(0));
        verify(sportRepository).findSportsByCriteria(
                eq(sportDto.id()),
                eq(sportDto.sportName()),
                eq(pageable)
        );
        verify(sportMapper).toDto(sport);
    }

    @Test
    void createSport_Success() {
        // Given
        when(sportMapper.toModel(sportRequestDto)).thenReturn(sport);
        when(sportRepository.save(sport)).thenReturn(sport);
        when(sportMapper.toDto(sport)).thenReturn(sportDto);

        // When
        SportDto result = sportService.createSport(sportRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(sportDto, result);
        verify(sportMapper).toModel(sportRequestDto);
        verify(sportRepository).save(sport);
        verify(sportMapper).toDto(sport);
    }

    @Test
    void updateSport_Success() {
        // Given
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(sportRepository.findBySportName("UpdatedSport")).thenReturn(Optional.empty());
        when(sportRepository.save(sport)).thenReturn(sport);
        when(sportMapper.toDto(sport)).thenReturn(new SportDto(1L, "UpdatedSport"));

        SportRequestDto updatedRequestDto = new SportRequestDto("UpdatedSport");

        // When
        SportDto result = sportService.updateSport(1L, updatedRequestDto);

        // Then
        assertNotNull(result);
        assertEquals("UpdatedSport", result.sportName());
        verify(sportRepository).findById(1L);
        verify(sportRepository).findBySportName("UpdatedSport");
        verify(sportRepository).save(sport);
        verify(sportMapper).toDto(sport);
    }

    @Test
    void updateSport_ExistingSportName_ThrowsIllegalArgumentException() {
        // Given
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(sportRepository.findBySportName("Basketball")).thenReturn(Optional.of(new Sport()));

        SportRequestDto conflictingRequestDto = new SportRequestDto("Basketball");

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sportService.updateSport(1L, conflictingRequestDto));
        assertEquals("Sport with name Basketball already exists", exception.getMessage());
        verify(sportRepository).findById(1L);
        verify(sportRepository).findBySportName("Basketball");
        verify(sportRepository, never()).save(any());
        verify(sportMapper, never()).toDto(any());
    }

    @Test
    void deleteSport_Success() {
        // Given
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));

        // When
        sportService.deleteSport(1L);

        // Then
        verify(sportRepository).findById(1L);
        verify(sportRepository).deleteById(1L);
    }
}
