package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.model.Event;
import com.example.trainup.model.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "requestDto.name", target = "name")
    @Mapping(source = "requestDto.description", target = "description")
    @Mapping(source = "requestDto.dateTime", target = "dateTime")
    @Mapping(source = "sport", target = "sport")
    Event toModel(EventRegistrationRequestDto requestDto, Sport sport);

    @Mapping(source = "sport.id", target = "sportId")
    @Mapping(source = "gym.id", target = "gymId")
    @Mapping(source = "trainer.id", target = "trainerId")
    EventResponseDto toDto(Event event);
}
