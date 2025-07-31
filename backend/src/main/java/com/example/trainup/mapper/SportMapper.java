package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.model.Sport;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SportMapper {
    Sport toModel(SportRequestDto requestDto);

    SportDto toDto(Sport sport);
}
