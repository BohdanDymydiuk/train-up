package com.example.trainup.service;

import com.example.trainup.dto.AddressDto;
import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import com.example.trainup.mapper.EventMapper;
import com.example.trainup.model.Address;
import com.example.trainup.model.Event;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.EventRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class EventServiceImpl implements EventService {
    private static final String SPORT_NOT_FOUND_MSG = "Sport not found with id: ";

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final TrainerRepository trainerRepository;
    private final SportRepository sportRepository;
    private final GymRepository gymRepository;
    private final AddressRepository addressRepository;

    @Override
    public EventResponseDto createEventByTrainer(Authentication authentication,
                                                 EventRegistrationRequestDto requestDto) {
        String email = authentication.getName();
        Trainer trainer = trainerRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Trainer not found with email: " + email));

        Sport sport = sportRepository.findById(requestDto.sportId()).orElseThrow(() ->
                new EntityNotFoundException(SPORT_NOT_FOUND_MSG + requestDto.sportId()));
        Event event = eventMapper.toModel(requestDto, sport);
        event.setTrainer(trainer);
        if (event.getLocation() != null) {
            Address existingAddress = addressRepository.findByCountryAndCityAndStreetAndHouse(
                    event.getLocation().getCountry(),
                    event.getLocation().getCity(),
                    event.getLocation().getStreet(),
                    event.getLocation().getHouse()
            ).orElseGet(() -> addressRepository.save(event.getLocation()));
            event.setLocation(existingAddress);
        }
        Event savedEvent = eventRepository.save(event);

        log.info("Event created by trainer with id: {}", savedEvent.getId());
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventResponseDto createEventByGymOwner(
            Authentication authentication,
            EventRegistrationRequestDto requestDto,
            Long gymId
    ) {
        String email = authentication.getName();
        UserCredentials user = userCredentialsRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User not found with email: " + email));
        Gym gym = gymRepository.findById(gymId).orElseThrow(() ->
                new EntityNotFoundException("Gym not found with id: " + gymId));
        if (!gym.getGymOwner().getUserCredentials().getId().equals(user.getId())) {
            throw new IllegalStateException("Gym does not belong to this owner");
        }

        Sport sport = sportRepository.findById(requestDto.sportId()).orElseThrow(() ->
                new EntityNotFoundException(SPORT_NOT_FOUND_MSG + requestDto.sportId()));
        Event event = eventMapper.toModel(requestDto, sport);
        event.setGym(gym);
        if (event.getLocation() != null) {
            Address existingAddress = addressRepository.findByCountryAndCityAndStreetAndHouse(
                    event.getLocation().getCountry(),
                    event.getLocation().getCity(),
                    event.getLocation().getStreet(),
                    event.getLocation().getHouse()
            ).orElseGet(() -> addressRepository.save(event.getLocation()));
            event.setLocation(existingAddress);
        }
        Event savedEvent = eventRepository.save(event);

        log.info("Event created by gym owner for gymId: {}, eventId: {}",
                gymId, savedEvent.getId());
        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents(EventFilterRequestDto filter, Pageable pageable) {
        LocalDateTime startOfDay = filter.date() != null ? filter.date().atStartOfDay() : null;
        LocalDateTime endOfDay = filter.date() != null
                ? filter.date().plusDays(1).atStartOfDay() : null;

        Page<Event> eventPage = eventRepository.findEventsByCriteria(
                filter.id(),
                filter.name(),
                filter.sportId(),
                startOfDay,
                endOfDay,
                filter.gymId(),
                filter.trainerId(),
                filter.onlineTraining(),
                filter.intensity(),
                filter.locationCountry(),
                filter.locationCity(),
                filter.locationCityDistrict(),
                filter.locationStreet(),
                filter.locationHouse(),
                pageable
        );

        log.info("Found {} events with filter: {}", eventPage.getTotalElements(), filter);
        return eventPage.stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Override
    public EventResponseDto updateEvent(Long id, EventUpdateRequestDto requestDto) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Event not found with id: " + id));

        Optional.ofNullable(requestDto.name()).ifPresent(existingEvent::setName);
        Optional.ofNullable(requestDto.sportId()).ifPresent(sportId -> {
            Sport sport = sportRepository.findById(sportId)
                    .orElseThrow(() -> new EntityNotFoundException(SPORT_NOT_FOUND_MSG
                            + sportId));
            existingEvent.setSport(sport);
        });
        Optional.ofNullable(requestDto.description()).ifPresent(existingEvent::setDescription);
        Optional.ofNullable(requestDto.dateTime()).ifPresent(existingEvent::setDateTime);
        Optional.ofNullable(requestDto.durationInMin()).ifPresent(existingEvent::setDurationInMin);

        updateLocation(existingEvent, requestDto.location());

        Event savedEvent = eventRepository.save(existingEvent);

        log.info("Updated event with id: {}", id);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserModifyEvent(String email, Long eventId) {
        if (email == null) {
            log.debug("Email is null, access denied");
            return false;
        }

        UserCredentials user = userCredentialsRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.debug("User with email {} not found", email);
            return false;
        }

        Event event = eventRepository.findByIdWithDetails(eventId).orElse(null);
        if (event == null) {
            log.debug("Event with ID {} not found", eventId);
            return false;
        }

        boolean canModify = (event.getTrainer() != null
                && event.getTrainer().getUserCredentials() != null
                && email.equals(event.getTrainer().getUserCredentials().getEmail()))
                ||
                (event.getGym() != null && event.getGym().getGymOwner() != null
                        && event.getGym().getGymOwner().getUserCredentials() != null
                        && email.equals(event.getGym().getGymOwner().getUserCredentials()
                        .getEmail()));
        log.debug("User {} can modify event {}: {}", email, eventId, canModify);
        return canModify;
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Deleted event with id: {}", id);
    }

    private void updateLocation(Event event, AddressDto locationDto) {
        if (locationDto != null) {
            Address address = event.getLocation();
            if (address == null) {
                throw new IllegalStateException("Event with ID " + event.getId()
                        + " does not have a location, which is required.");
            }
            Optional.ofNullable(locationDto.country()).ifPresent(address::setCountry);
            Optional.ofNullable(locationDto.city()).ifPresent(address::setCity);
            Optional.ofNullable(locationDto.cityDistrict()).ifPresent(address::setCityDistrict);
            Optional.ofNullable(locationDto.street()).ifPresent(address::setStreet);
            Optional.ofNullable(locationDto.house()).ifPresent(address::setHouse);
            addressRepository.save(address);
        }
    }
}
