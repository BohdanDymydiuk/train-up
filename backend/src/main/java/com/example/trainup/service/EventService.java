package com.example.trainup.service;

import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface EventService {
    EventResponseDto createEventByTrainer(Authentication authentication,
                                          EventRegistrationRequestDto requestDto);

    EventResponseDto createEventByGymOwner(Authentication authentication,
                                           EventRegistrationRequestDto requestDto,
                                           Long gymId);

    List<EventResponseDto> getAllEvents(EventFilterRequestDto filterRequestDto, Pageable pageable);

    EventResponseDto updateEvent(Long id, EventUpdateRequestDto requestDto);

    boolean canUserModifyEvent(String email, Long eventId);

    void deleteEvent(Long id);
}
