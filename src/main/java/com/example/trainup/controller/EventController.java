package com.example.trainup.controller;

import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import com.example.trainup.service.EventService;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
@Log4j2
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/trainer")
    public EventResponseDto createEventByTrainer(
            @RequestBody EventRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Creating event by trainer with request: {}", requestDto);
        EventResponseDto responseDto = eventService
                .createEventByTrainer(authentication, requestDto);
        return responseDto;
    }

    @PreAuthorize("hasRole('GYM_OWNER')")
    @PostMapping("/gym/{gymId}")
    public EventResponseDto createEventByGymOwner(
            @PathVariable @Positive Long gymId,
            @RequestBody EventRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Creating event by gym owner for gymId: {}, request: {}", gymId, requestDto);
        EventResponseDto responseDto = eventService
                .createEventByGymOwner(authentication, requestDto, gymId);
        return responseDto;
    }

    @GetMapping
    public List<EventResponseDto> getAllEvents(
            @RequestParam(required = false) @Positive Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @Positive Long sportId,
            @RequestParam(required = false) LocalDateTime dateTime,
            @RequestParam(required = false) @Positive Long gymId,
            @RequestParam(required = false) @Positive Long trainerId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        EventFilterRequestDto filterRequestDto = new EventFilterRequestDto(
                id, name, sportId, dateTime, gymId, trainerId);
        log.info("Fetching events with filter: {}, pageable: {}", filterRequestDto, pageable);

        List<EventResponseDto> responseDtos = eventService.getAllEvents(filterRequestDto, pageable);
        return responseDtos;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@eventServiceImpl.canUserModifyEvent(#authentication.name, #id)")
    public EventResponseDto updateEvent(
            @PathVariable @Positive Long id,
            @RequestBody EventUpdateRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Updating event with id: {}, request: {}", id, requestDto);
        EventResponseDto responseDto = eventService.updateEvent(id, requestDto);
        return responseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@eventServiceImpl.canUserModifyEvent(#authentication.name, #id)")
    public ResponseEntity<Void> deleteEvent(@PathVariable @Positive Long id,
                                            Authentication authentication) {
        log.info("Deleting event with id: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
