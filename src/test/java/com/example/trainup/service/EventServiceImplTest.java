package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import com.example.trainup.mapper.EventMapper;
import com.example.trainup.model.Event;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.EventRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private GymRepository gymRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventRegistrationRequestDto registrationRequestDto;
    private EventResponseDto eventResponseDto;
    private EventUpdateRequestDto updateRequestDto;
    private EventFilterRequestDto filterRequestDto;
    private Event event;
    private Trainer trainer;
    private Gym gym;
    private Sport sport;
    private UserCredentials userCredentials;
    private Authentication authentication;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new EventRegistrationRequestDto(
                "Test Event",
                1L,
                "Event description",
                LocalDateTime.of(2025, 7, 1, 10, 0)
        );

        eventResponseDto = new EventResponseDto(
                1L,
                "Test Event",
                1L,
                "Event description",
                LocalDateTime.of(2025, 7, 1, 10, 0),
                1L,
                1L
        );

        updateRequestDto = new EventUpdateRequestDto(
                "Updated Event",
                2L,
                "Updated description",
                LocalDateTime.of(2025, 7, 2, 12, 0)
        );

        filterRequestDto = new EventFilterRequestDto(
                1L,
                "Test Event",
                1L,
                LocalDate.of(2025, 7, 1),
                1L,
                1L
        );

        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDescription("Event description");
        event.setDateTime(LocalDateTime.of(2025, 7, 1, 10, 0));

        sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Boxing");
        event.setSport(sport);

        trainer = new Trainer();
        trainer.setId(1L);
        event.setTrainer(trainer);

        gym = new Gym();
        gym.setId(1L);
        event.setGym(gym);

        userCredentials = new UserCredentials();
        userCredentials.setId(1L);
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        trainer.setUserCredentials(userCredentials);
        gym.setGymOwner(new GymOwner());
        gym.getGymOwner().setUserCredentials(userCredentials);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createEventByTrainer_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(trainer));
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(eventMapper.toModel(registrationRequestDto, sport)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventResponseDto);

        // When
        EventResponseDto result = eventService
                .createEventByTrainer(authentication, registrationRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(eventResponseDto, result);
        verify(authentication).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(1L);
        verify(eventMapper).toModel(registrationRequestDto, sport);
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(authentication, trainerRepository, sportRepository, eventMapper,
                eventRepository);
        verifyNoInteractions(userCredentialsRepository, gymRepository);
    }

    @Test
    void createEventByTrainer_TrainerNotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService.createEventByTrainer(authentication, registrationRequestDto));
        assertEquals("Trainer not found with email: john.doe@example.com", exception.getMessage());
        verify(authentication).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verifyNoMoreInteractions(authentication, trainerRepository);
        verifyNoInteractions(sportRepository, eventMapper, eventRepository,
                userCredentialsRepository, gymRepository);
    }

    @Test
    void createEventByTrainer_SportNotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(trainer));
        when(sportRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService.createEventByTrainer(authentication, registrationRequestDto));
        assertEquals("Sport not found with id: 1", exception.getMessage());
        verify(authentication).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(1L);
        verifyNoMoreInteractions(authentication, trainerRepository, sportRepository);
        verifyNoInteractions(eventMapper, eventRepository, userCredentialsRepository,
                gymRepository);
    }

    @Test
    void createEventByGymOwner_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(eventMapper.toModel(registrationRequestDto, sport)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventResponseDto);

        // When
        EventResponseDto result = eventService
                .createEventByGymOwner(authentication, registrationRequestDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(eventResponseDto, result);
        verify(authentication).getName();
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(gymRepository).findById(1L);
        verify(sportRepository).findById(1L);
        verify(eventMapper).toModel(registrationRequestDto, sport);
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(authentication, userCredentialsRepository, gymRepository,
                sportRepository, eventMapper, eventRepository);
        verifyNoInteractions(trainerRepository);
    }

    @Test
    void createEventByGymOwner_UserNotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService
                        .createEventByGymOwner(authentication, registrationRequestDto, 1L));
        assertEquals("User not found with email: john.doe@example.com", exception.getMessage());
        verify(authentication).getName();
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verifyNoMoreInteractions(authentication, userCredentialsRepository);
        verifyNoInteractions(gymRepository, sportRepository, eventMapper, eventRepository,
                trainerRepository);
    }

    @Test
    void createEventByGymOwner_GymNotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(gymRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService
                        .createEventByGymOwner(authentication, registrationRequestDto, 1L));
        assertEquals("Gym not found with id: 1", exception.getMessage());
        verify(authentication).getName();
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(gymRepository).findById(1L);
        verifyNoMoreInteractions(authentication, userCredentialsRepository, gymRepository);
        verifyNoInteractions(sportRepository, eventMapper, eventRepository, trainerRepository);
    }

    @Test
    void createEventByGymOwner_GymNotOwned_ThrowsIllegalStateException() {
        // Given
        UserCredentials otherUser = new UserCredentials();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        gym.getGymOwner().setUserCredentials(otherUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> eventService
                        .createEventByGymOwner(authentication, registrationRequestDto, 1L));
        assertEquals("Gym does not belong to this owner", exception.getMessage());
        verify(authentication).getName();
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(gymRepository).findById(1L);
        verifyNoMoreInteractions(authentication, userCredentialsRepository, gymRepository);
        verifyNoInteractions(sportRepository, eventMapper, eventRepository, trainerRepository);
    }

    @Test
    void createEventByGymOwner_SportNotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
        when(sportRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService
                        .createEventByGymOwner(authentication, registrationRequestDto, 1L));
        assertEquals("Sport not found with id: 1", exception.getMessage());
        verify(authentication).getName();
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(gymRepository).findById(1L);
        verify(sportRepository).findById(1L);
        verifyNoMoreInteractions(authentication, userCredentialsRepository, gymRepository,
                sportRepository);
        verifyNoInteractions(eventMapper, eventRepository, trainerRepository);
    }

    @Test
    void getAllEvents_Success() {
        // Given
        Page<Event> eventPage = new PageImpl<>(List.of(event), pageable, 1);
        LocalDateTime startOfDay = LocalDate.of(2025, 7, 1).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.of(2025, 7, 2).atStartOfDay();
        when(eventRepository.findEventsByCriteria(
                eq(1L),
                eq("Test Event"),
                eq(1L),
                eq(startOfDay),
                eq(endOfDay),
                eq(1L),
                eq(1L),
                eq(pageable))
        ).thenReturn(eventPage);
        when(eventMapper.toDto(event)).thenReturn(eventResponseDto);

        // When
        List<EventResponseDto> result = eventService.getAllEvents(filterRequestDto, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventResponseDto, result.get(0));
        verify(eventRepository).findEventsByCriteria(
                eq(1L),
                eq("Test Event"),
                eq(1L),
                eq(startOfDay),
                eq(endOfDay),
                eq(1L),
                eq(1L),
                eq(pageable)
        );
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepository, eventMapper);
        verifyNoInteractions(userCredentialsRepository, trainerRepository, sportRepository,
                gymRepository);
    }

    @Test
    void updateEvent_Success() {
        // Given
        Sport updatedSport = new Sport();
        updatedSport.setId(2L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(updatedSport));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventResponseDto);

        // When
        EventResponseDto result = eventService.updateEvent(1L, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(eventResponseDto, result);
        verify(eventRepository).findById(1L);
        verify(sportRepository).findById(2L);
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepository, sportRepository, eventMapper);
        verifyNoInteractions(userCredentialsRepository, trainerRepository, gymRepository);
    }

    @Test
    void updateEvent_EventNotFound_ThrowsEntityNotFoundException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService.updateEvent(999L, updateRequestDto));
        assertEquals("Event not found with id: 999", exception.getMessage());
        verify(eventRepository).findById(999L);
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(sportRepository, eventMapper, userCredentialsRepository,
                trainerRepository, gymRepository);
    }

    @Test
    void updateEvent_SportNotFound_ThrowsEntityNotFoundException() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(sportRepository.findById(2L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService.updateEvent(1L, updateRequestDto));
        assertEquals("Sport not found with id: 2", exception.getMessage());
        verify(eventRepository).findById(1L);
        verify(sportRepository).findById(2L);
        verifyNoMoreInteractions(eventRepository, sportRepository);
        verifyNoInteractions(eventMapper, userCredentialsRepository, trainerRepository,
                gymRepository);
    }

    @Test
    void canUserModifyEvent_Trainer_Success() {
        // Given
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(eventRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.canUserModifyEvent("john.doe@example.com", 1L);

        // Then
        assertTrue(result);
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(eventRepository).findByIdWithDetails(1L);
        verifyNoMoreInteractions(userCredentialsRepository, eventRepository);
        verifyNoInteractions(trainerRepository, sportRepository, gymRepository, eventMapper);
    }

    @Test
    void canUserModifyEvent_GymOwner_Success() {
        // Given
        event.setTrainer(null); // Прибираємо тренера, щоб перевірити власника спортзалу
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(eventRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.canUserModifyEvent("john.doe@example.com", 1L);

        // Then
        assertTrue(result);
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(eventRepository).findByIdWithDetails(1L);
        verifyNoMoreInteractions(userCredentialsRepository, eventRepository);
        verifyNoInteractions(trainerRepository, sportRepository, gymRepository, eventMapper);
    }

    @Test
    void canUserModifyEvent_NullEmail_ReturnsFalse() {
        // When
        boolean result = eventService.canUserModifyEvent(null, 1L);

        // Then
        assertFalse(result);
        verifyNoInteractions(userCredentialsRepository, eventRepository, trainerRepository,
                sportRepository, gymRepository, eventMapper);
    }

    @Test
    void canUserModifyEvent_UserNotFound_ReturnsFalse() {
        // Given
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.empty());

        // When
        boolean result = eventService.canUserModifyEvent("john.doe@example.com", 1L);

        // Then
        assertFalse(result);
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verifyNoMoreInteractions(userCredentialsRepository);
        verifyNoInteractions(eventRepository, trainerRepository, sportRepository, gymRepository,
                eventMapper);
    }

    @Test
    void canUserModifyEvent_EventNotFound_ReturnsFalse() {
        // Given
        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(eventRepository.findByIdWithDetails(1L)).thenReturn(Optional.empty());

        // When
        boolean result = eventService.canUserModifyEvent("john.doe@example.com", 1L);

        // Then
        assertFalse(result);
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(eventRepository).findByIdWithDetails(1L);
        verifyNoMoreInteractions(userCredentialsRepository, eventRepository);
        verifyNoInteractions(trainerRepository, sportRepository, gymRepository, eventMapper);
    }

    @Test
    void canUserModifyEvent_Unauthorized_ReturnsFalse() {
        // Given
        UserCredentials otherUser = new UserCredentials();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        trainer.setUserCredentials(otherUser);
        gym.getGymOwner().setUserCredentials(otherUser);

        when(userCredentialsRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(eventRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(event));

        // When
        boolean result = eventService.canUserModifyEvent("john.doe@example.com", 1L);

        // Then
        assertFalse(result);
        verify(userCredentialsRepository).findByEmail("john.doe@example.com");
        verify(eventRepository).findByIdWithDetails(1L);
        verifyNoMoreInteractions(userCredentialsRepository, eventRepository);
        verifyNoInteractions(trainerRepository, sportRepository, gymRepository, eventMapper);
    }

    @Test
    void deleteEvent_Success() {
        // Given
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);

        // When
        eventService.deleteEvent(1L);

        // Then
        verify(eventRepository).existsById(1L);
        verify(eventRepository).deleteById(1L);
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(userCredentialsRepository, trainerRepository, sportRepository,
                gymRepository, eventMapper);
    }

    @Test
    void deleteEvent_NotFound_ThrowsEntityNotFoundException() {
        // Given
        when(eventRepository.existsById(999L)).thenReturn(false);

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventService.deleteEvent(999L));
        assertEquals("Event not found with id: 999", exception.getMessage());
        verify(eventRepository).existsById(999L);
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(userCredentialsRepository, trainerRepository, sportRepository,
                gymRepository, eventMapper);
    }
}
