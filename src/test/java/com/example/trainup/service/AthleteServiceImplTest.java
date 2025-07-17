package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import com.example.trainup.mapper.AthleteMapper;
import com.example.trainup.model.Sport;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.service.users.AthleteServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AthleteServiceImplTest {
    @Mock
    private AthleteMapper athleteMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private UserCredentialService userCredentialService;

    @InjectMocks
    private AthleteServiceImpl athleteService;

    private AthleteRegistrationRequestDto registrationRequestDto;
    private AthleteResponseDto athleteResponseDto;
    private AthleteUpdateRequestDto updateRequestDto;
    private Athlete athlete;
    private UserCredentials userCredentials;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe", Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "Password123!",
                "Password123!",
                Set.of("123456789"),
                Set.of(1L),
                true,
                true
        );

        athleteResponseDto = new AthleteResponseDto(
                1L,
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "ATHLETE",
                Set.of("123456789"),
                Set.of(1L),
                true,
                true
        );

        updateRequestDto = new AthleteUpdateRequestDto(
                "Jane",
                "Doe",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/new-image.jpg",
                Set.of("987654321"),
                Set.of(2L),
                false,
                false
        );

        athlete = new Athlete();
        athlete.setId(1L);
        athlete.setFirstName("John");
        athlete.setLastName("Doe");
        athlete.setGender(Gender.MALE);
        athlete.setDateOfBirth(LocalDate.of(1990, 1, 1));
        athlete.setProfileImageUrl("http://example.com/image.jpg");
        athlete.setPhoneNumbers(Set.of("123456789"));
        athlete.setSports(Set.of(new Sport()));
        athlete.setEmailPermission(true);
        athlete.setPhonePermission(true);

        userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.ATHLETE);
        athlete.setUserCredentials(userCredentials);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void register_Success() {
        // Given
        when(athleteMapper.toModel(
                eq(registrationRequestDto),
                any(PasswordEncoder.class),
                any(SportRepository.class))
        ).thenReturn(athlete);
        when(athleteRepository.save(any(Athlete.class))).thenReturn(athlete);
        when(athleteMapper.toDto(athlete)).thenReturn(athleteResponseDto);

        // When
        AthleteResponseDto result = athleteService.register(registrationRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(athleteResponseDto, result);
        verify(athleteMapper).toModel(
                eq(registrationRequestDto),
                any(PasswordEncoder.class),
                any(SportRepository.class)
        );
        verify(athleteRepository).save(athlete);
        verify(userCredentialService).assignRoleBasedOnUserType(userCredentials);
        verify(athleteMapper).toDto(athlete);
    }

    @Test
    void getAthleteById_Success() {
        // Given
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(athlete));
        when(athleteMapper.toDto(athlete)).thenReturn(athleteResponseDto);

        // When
        AthleteResponseDto result = athleteService.getAthleteById(1L);

        // Then
        assertNotNull(result);
        assertEquals(athleteResponseDto, result);
        verify(athleteRepository).findById(1L);
        verify(athleteMapper).toDto(athlete);
    }

    @Test
    void getAthleteById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        when(athleteRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> athleteService.getAthleteById(999L));
        assertEquals("Can not find Athlete by id: 999", exception.getMessage());
        verify(athleteRepository).findById(999L);
        verifyNoInteractions(athleteMapper);
    }

    @Test
    void canUserModifyAthlete_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(athlete));

        // When
        boolean result = athleteService.canUserModifyAthlete(authentication, 1L);

        // Then
        assertTrue(result);
        verify(authentication).getName();
        verify(athleteRepository).findById(1L);
    }

    @Test
    void canUserModifyAthlete_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(athleteRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> athleteService.canUserModifyAthlete(authentication, 999L));
        assertEquals("Can not find Athlete by id: 999", exception.getMessage());
        verify(authentication).getName();
        verify(athleteRepository).findById(999L);
    }

    @Test
    void canUserModifyAthlete_Unauthorized_ReturnsFalse() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("other@example.com");
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(athlete));

        // When
        boolean result = athleteService.canUserModifyAthlete(authentication, 1L);

        // Then
        assertFalse(result);
        verify(authentication).getName();
        verify(athleteRepository).findById(1L);
    }

    @Test
    void canUserModifyAthlete_NullAuthentication_ReturnsFalse() {
        // When
        boolean result = athleteService.canUserModifyAthlete(null, 1L);

        // Then
        assertFalse(result);
        verifyNoInteractions(athleteRepository);
    }

    @Test
    void getAllAthlete_Success() {
        // Given
        AthleteFilterRequestDto filter = new AthleteFilterRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                Set.of(1L),
                true,
                true
        );
        Page<Athlete> athletePage = new PageImpl<>(List.of(athlete), pageable, 1);
        when(athleteRepository.findAthleteByCriteria(
                eq("John"),
                eq("Doe"),
                eq(Gender.MALE),
                eq(LocalDate.of(1990, 1, 1)),
                eq(Set.of(1L)),
                eq(true),
                eq(true),
                eq(pageable))
        ).thenReturn(athletePage);
        when(athleteMapper.toDto(athlete)).thenReturn(athleteResponseDto);

        // When
        List<AthleteResponseDto> result = athleteService.getAllAthlete(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(athleteResponseDto, result.get(0));
        verify(athleteRepository).findAthleteByCriteria(
                eq("John"),
                eq("Doe"),
                eq(Gender.MALE),
                eq(LocalDate.of(1990, 1, 1)),
                eq(Set.of(1L)),
                eq(true),
                eq(true),
                eq(pageable)
        );
        verify(athleteMapper).toDto(athlete);
    }

    @Test
    void deleteAthleteById_Success() {
        // Given
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(athlete));
        doNothing().when(athleteRepository).deleteById(1L);

        // When
        athleteService.deleteAthleteById(1L);

        // Then
        verify(athleteRepository).findById(1L);
        verify(athleteRepository).deleteById(1L);
    }

    @Test
    void deleteAthleteById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        when(athleteRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> athleteService.deleteAthleteById(999L));
        assertEquals("Can not find Athlete by id:999", exception.getMessage());
        verify(athleteRepository).findById(999L);
        verify(athleteRepository, never()).deleteById(any());
    }

    @Test
    void updateAthleteByAuth_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(athleteRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(athlete));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(new Sport()));
        when(athleteRepository.save(any(Athlete.class))).thenReturn(athlete);
        when(athleteMapper.toDto(athlete)).thenReturn(athleteResponseDto);

        // When
        AthleteResponseDto result = athleteService
                .updateAthleteByAuth(authentication, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(athleteResponseDto, result);
        verify(authentication, times(2)).getName();
        verify(athleteRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(2L);
        verify(athleteRepository).save(any(Athlete.class));
        verify(athleteMapper).toDto(athlete);
    }

    @Test
    void updateAthleteByAuth_NullAuthentication_ThrowsIllegalStateException() {
        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> athleteService.updateAthleteByAuth(null, updateRequestDto));
        assertEquals("User is not authenticated", exception.getMessage());
        verifyNoInteractions(athleteRepository, sportRepository, athleteMapper);
    }

    @Test
    void updateAthleteByAuth_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(athleteRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> athleteService.updateAthleteByAuth(authentication, updateRequestDto));
        assertEquals("Athlete not found for email: john.doe@example.com", exception.getMessage());
        verify(authentication, times(2)).getName();
        verify(athleteRepository).findByEmail("john.doe@example.com");
        verifyNoInteractions(sportRepository, athleteMapper);
    }

    @Test
    void updateAthleteByAuth_InvalidSportId_ThrowsIllegalArgumentException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(athleteRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(athlete));
        when(sportRepository.findById(2L)).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> athleteService.updateAthleteByAuth(authentication, updateRequestDto));
        assertEquals("Sport with id 2 not found", exception.getMessage());
        verify(authentication, times(2)).getName();
        verify(athleteRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(2L);
        verifyNoMoreInteractions(athleteRepository, athleteMapper);
    }
}
