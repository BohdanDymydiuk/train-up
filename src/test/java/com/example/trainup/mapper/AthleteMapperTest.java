package com.example.trainup.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.SportRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AthleteMapperTest {
    private AthleteMapper athleteMapper;
    private SportRepository sportRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        sportRepository = Mockito.mock(SportRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        athleteMapper = Mappers.getMapper(AthleteMapper.class);
    }

    @Test
    void toDto_Success() {
        // Given
        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setFirstName("John");
        athlete.setLastName("Doe");
        athlete.setGender(Gender.MALE);
        athlete.setDateOfBirth(LocalDate.of(1990, 1, 1));
        athlete.setProfileImageUrl("http://image.jpg");
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.ATHLETE);
        athlete.setUserCredentials(userCredentials);
        Set<Sport> sports = new HashSet<>();
        Sport sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Football");
        sports.add(sport);
        athlete.setSports(sports);
        athlete.setPhoneNumbers(new HashSet<>(Set.of("+380501234567")));
        athlete.setEmailPermission(true);
        athlete.setPhonePermission(false);

        // When
        AthleteResponseDto result = athleteMapper.toDto(athlete);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals(Gender.MALE, result.gender());
        assertEquals(LocalDate.of(1990, 1, 1), result.dateOfBirth());
        assertEquals("http://image.jpg", result.profileImageUrl());
        assertEquals("john.doe@example.com", result.email());
        assertEquals("ATHLETE", result.userType());
        assertEquals(Set.of("+380501234567"), result.phoneNumbers());
        assertEquals(Set.of(1L), result.sportIds());
        assertTrue(result.emailPermission());
        assertFalse(result.phonePermission());
    }

    @Test
    void toModel_Success() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Football");

        AthleteRegistrationRequestDto requestDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://image.jpg",
                "john.doe@example.com",
                "password123",
                "password123",
                Set.of("+380501234567"),
                Set.of(1L),
                true,
                false
        );

        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // When
        Athlete athlete = athleteMapper.toModel(requestDto, passwordEncoder, sportRepository);

        // Then
        assertNotNull(athlete);
        assertNull(athlete.getId()); // ID ігнорується
        assertEquals("John", athlete.getFirstName());
        assertEquals("Doe", athlete.getLastName());
        assertEquals(Gender.MALE, athlete.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), athlete.getDateOfBirth());
        assertEquals("http://image.jpg", athlete.getProfileImageUrl());
        assertEquals(Set.of("+380501234567"), athlete.getPhoneNumbers());
        assertEquals(Set.of(sport), athlete.getSports());
        assertTrue(athlete.getEmailPermission());
        assertFalse(athlete.getPhonePermission());
        assertNotNull(athlete.getUserCredentials());
        assertEquals("john.doe@example.com", athlete.getUserCredentials().getEmail());
        assertEquals("encodedPassword", athlete.getUserCredentials().getPassword());
        assertEquals(UserCredentials.UserType.ATHLETE, athlete.getUserCredentials().getUserType());
    }

    @Test
    void toModel_WithEmptySportIds() {
        // Given
        AthleteRegistrationRequestDto requestDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://image.jpg",
                "john.doe@example.com",
                "password123",
                "password123",
                Set.of("+380501234567"),
                Set.of(),
                true,
                false
        );
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // When
        Athlete athlete = athleteMapper.toModel(requestDto, passwordEncoder, sportRepository);

        // Then
        assertNotNull(athlete);
        assertTrue(athlete.getSports().isEmpty());
    }

    @Test
    void toModel_WithNonExistentSportId_ThrowsIllegalArgumentException() {
        // Given
        AthleteRegistrationRequestDto requestDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://image.jpg",
                "john.doe@example.com",
                "password123",
                "password123",
                Set.of("+380501234567"),
                Set.of(999L), // Неіснуючий ID
                true,
                false
        );
        when(sportRepository.findById(eq(999L))).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> athleteMapper.toModel(requestDto, passwordEncoder, sportRepository));
    }
}
