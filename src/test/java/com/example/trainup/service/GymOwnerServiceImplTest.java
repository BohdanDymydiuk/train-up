package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;
import com.example.trainup.mapper.GymOwnerMapper;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.service.users.GymOwnerServiceImpl;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class GymOwnerServiceImplTest {
    @Mock
    private GymOwnerRepository gymOwnerRepository;

    @Mock
    private GymOwnerMapper gymOwnerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCredentialService userCredentialService;

    @InjectMocks
    private GymOwnerServiceImpl gymOwnerService;

    private GymOwnerRegistrationRequestDto registrationRequestDto;
    private GymOwnerResponseDto responseDto;
    private GymOwner gymOwner;
    private UserCredentials userCredentials;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new GymOwnerRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1985, 5, 15),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "Password123!",
                "Password123!",
                Set.of("123456789")
        );

        responseDto = new GymOwnerResponseDto(
                1L,
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1985, 5, 15),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "GYM_OWNER",
                Set.of("123456789"),
                Set.of()
        );

        gymOwner = new GymOwner();
        gymOwner.setId(1L);
        gymOwner.setFirstName("John");
        gymOwner.setLastName("Doe");
        gymOwner.setGender(Gender.MALE);
        gymOwner.setDateOfBirth(LocalDate.of(1985, 5, 15));
        gymOwner.setProfileImageUrl("http://example.com/image.jpg");
        gymOwner.setPhoneNumbers(Set.of("123456789"));
        gymOwner.setOwnedGyms(Set.of());

        userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.GYM_OWNER);
        gymOwner.setUserCredentials(userCredentials);
    }

    @Test
    void register_Success() {
        // Given
        when(gymOwnerMapper.toModel(eq(registrationRequestDto), any(PasswordEncoder.class)))
                .thenReturn(gymOwner);
        when(gymOwnerRepository.save(any(GymOwner.class))).thenReturn(gymOwner);
        when(gymOwnerMapper.toDto(gymOwner)).thenReturn(responseDto);

        // When
        GymOwnerResponseDto result = gymOwnerService.register(registrationRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(gymOwnerMapper).toModel(eq(registrationRequestDto), any(PasswordEncoder.class));
        verify(gymOwnerRepository).save(gymOwner);
        verify(userCredentialService).assignRoleBasedOnUserType(userCredentials);
        verify(gymOwnerMapper).toDto(gymOwner);
    }
}
