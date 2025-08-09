package com.example.trainup.mapper;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.model.Sport;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-09T16:34:34+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class SportMapperImpl implements SportMapper {

    @Override
    public Sport toModel(SportRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Sport sport = new Sport();

        sport.setSportName( requestDto.sportName() );

        return sport;
    }

    @Override
    public SportDto toDto(Sport sport) {
        if ( sport == null ) {
            return null;
        }

        Long id = null;
        String sportName = null;

        id = sport.getId();
        sportName = sport.getSportName();

        SportDto sportDto = new SportDto( id, sportName );

        return sportDto;
    }
}
