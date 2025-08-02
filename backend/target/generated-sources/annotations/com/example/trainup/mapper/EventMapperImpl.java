package com.example.trainup.mapper;

import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.model.Event;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.Trainer;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T19:12:59+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public Event toModel(EventRegistrationRequestDto requestDto, Sport sport) {
        if ( requestDto == null && sport == null ) {
            return null;
        }

        Event event = new Event();

        if ( requestDto != null ) {
            event.setName( requestDto.name() );
            event.setDescription( requestDto.description() );
            event.setDateTime( requestDto.dateTime() );
        }
        event.setSport( sport );

        return event;
    }

    @Override
    public EventResponseDto toDto(Event event) {
        if ( event == null ) {
            return null;
        }

        Long sportId = null;
        Long gymId = null;
        Long trainerId = null;
        Long id = null;
        String name = null;
        String description = null;
        LocalDateTime dateTime = null;

        sportId = eventSportId( event );
        gymId = eventGymId( event );
        trainerId = eventTrainerId( event );
        id = event.getId();
        name = event.getName();
        description = event.getDescription();
        dateTime = event.getDateTime();

        EventResponseDto eventResponseDto = new EventResponseDto( id, name, sportId, description, dateTime, gymId, trainerId );

        return eventResponseDto;
    }

    private Long eventSportId(Event event) {
        if ( event == null ) {
            return null;
        }
        Sport sport = event.getSport();
        if ( sport == null ) {
            return null;
        }
        Long id = sport.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long eventGymId(Event event) {
        if ( event == null ) {
            return null;
        }
        Gym gym = event.getGym();
        if ( gym == null ) {
            return null;
        }
        Long id = gym.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long eventTrainerId(Event event) {
        if ( event == null ) {
            return null;
        }
        Trainer trainer = event.getTrainer();
        if ( trainer == null ) {
            return null;
        }
        Long id = trainer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
