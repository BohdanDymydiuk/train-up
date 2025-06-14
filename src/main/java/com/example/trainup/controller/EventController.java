package com.example.trainup.controller;

import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import com.example.trainup.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
@Log4j2
@Tag(
        name = "Event Management",
        description = "Endpoints for creating, retrieving, updating, and deleting events. "
                + "Restricted to Trainer or Gym Owner roles."
)
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Event by Trainer",
            description = "Allows a Trainer to create a new event. The trainer is automatically "
                    + "associated with the event."
    )
    public EventResponseDto createEventByTrainer(
            @RequestBody EventRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Attempting to create event by trainer '{}' with request: {}",
                authentication.getName(), requestDto);
        EventResponseDto responseDto = eventService
                .createEventByTrainer(authentication, requestDto);

        log.info("Event successfully created by trainer '{}'. Event ID: {}",
                authentication.getName(), responseDto.id());
        return responseDto;
    }

    @PreAuthorize("hasRole('GYM_OWNER')")
    @PostMapping("/gym/{gymId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Event by Gym Owner for a specific Gym",
            description = "Allows a Gym Owner to create a new event and associate it with a "
                    + "specific gym they own."
    )
    public EventResponseDto createEventByGymOwner(
            @PathVariable @Positive Long gymId,
            @RequestBody EventRegistrationRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Attempting to create event by gym owner '{}' for gymId: {}, with request: {}",
                authentication.getName(), gymId, requestDto);
        EventResponseDto responseDto = eventService
                .createEventByGymOwner(authentication, requestDto, gymId);

        log.info("Event successfully created by gym owner '{}' for gymId: {}. Event ID: {}",
                authentication.getName(), gymId, responseDto.id());
        return responseDto;
    }

    @GetMapping
    @Operation(
            summary = "Retrieve Events by Criteria",
            description = "Retrieves a paginated list of events based on various filtering "
                    + "criteria. Accessible to all authenticated users."
    )
    public List<EventResponseDto> getAllEvents(
            @RequestParam(required = false) @Positive Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @Positive Long sportId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) @Positive Long gymId,
            @RequestParam(required = false) @Positive Long trainerId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        EventFilterRequestDto filterRequestDto = new EventFilterRequestDto(
                id, name, sportId, date, gymId, trainerId);
        log.info("Attempting to fetch events with filter: {} and pageable: {}",
                filterRequestDto, pageable);

        List<EventResponseDto> responseDtos = eventService.getAllEvents(filterRequestDto, pageable);
        log.info("Successfully retrieved {} events with specified criteria.", responseDtos.size());
        return responseDtos;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@eventService.canUserModifyEvent(#authentication.name, #id)")
    @Operation(
            summary = "Update Event Details",
            description = "Allows the associated Trainer or Gym Owner to update specific details "
                    + "of an event."
    )
    public EventResponseDto updateEvent(
            @PathVariable @Positive Long id,
            @RequestBody EventUpdateRequestDto requestDto,
            Authentication authentication
    ) {
        log.info("Attempting to update event with ID: {} by user '{}' with request: {}",
                id, authentication.getName(), requestDto);
        EventResponseDto responseDto = eventService.updateEvent(id, requestDto);

        log.info("Event with ID: {} successfully updated.", id);
        return responseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@eventService.canUserModifyEvent(#authentication.name, #id)")
    @Operation(
            summary = "Delete Event",
            description = "Allows the associated Trainer or Gym Owner to delete an event."
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable @Positive Long id,
                                            Authentication authentication) {
        log.info("Attempting to delete event with ID: {} by user: {}",
                id, authentication.getName());
        eventService.deleteEvent(id);

        log.info("Event with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }
}
