package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.repository.TrainerRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    @Mock
    private GymOwnerRepository gymOwnerRepository;

    @Mock
    private AthleteRepository athleteRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CurrentUserService currentUserService;

    private UserCredentials userCredentials;
    private Athlete athlete;
    private GymOwner gymOwner;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);

        athlete = new Athlete();
        athlete.setId(1L);
        athlete.setFirstName("John");
        athlete.setLastName("Doe");
        athlete.setPhoneNumbers(Set.of("123456789"));
        athlete.setUserCredentials(userCredentials);
        athlete.setEmailPermission(true);
        athlete.setPhonePermission(true);

        gymOwner = new GymOwner();
        gymOwner.setId(1L);
        gymOwner.setFirstName("Jane");
        gymOwner.setLastName("Doe");
        gymOwner.setPhoneNumbers(Set.of("987654321"));
        gymOwner.setUserCredentials(userCredentials);

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setPhoneNumbers(Set.of("123456789"));
        trainer.setUserCredentials(userCredentials);
    }

    @Test
    void getCurrentUserByType_Trainer_Success() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(trainerRepository.findByUserCredentials(userCredentials))
                .thenReturn(Optional.of(trainer));

        // When
        Trainer result = currentUserService.getCurrentUserByType(Trainer.class);

        // Then
        assertNotNull(result);
        assertEquals(trainer, result);
        verify(authentication).getPrincipal();
        verify(trainerRepository).findByUserCredentials(userCredentials);
        verifyNoInteractions(gymOwnerRepository, athleteRepository);
    }

    @Test
    void getCurrentUserByType_Athlete_Success() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.ATHLETE);
        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(athleteRepository.findByUserCredentials(userCredentials))
                .thenReturn(Optional.of(athlete));

        // When
        Athlete result = currentUserService.getCurrentUserByType(Athlete.class);

        // Then
        assertNotNull(result);
        assertEquals(athlete, result);
        verify(authentication).getPrincipal();
        verify(athleteRepository).findByUserCredentials(userCredentials);
        verifyNoInteractions(gymOwnerRepository, trainerRepository);
    }

    @Test
    void getCurrentUserByType_GymOwner_Success() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.GYM_OWNER);
        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(gymOwnerRepository.findByUserCredentials(userCredentials))
                .thenReturn(Optional.of(gymOwner));

        // When
        GymOwner result = currentUserService.getCurrentUserByType(GymOwner.class);

        // Then
        assertNotNull(result);
        assertEquals(gymOwner, result);
        verify(authentication).getPrincipal();
        verify(gymOwnerRepository).findByUserCredentials(userCredentials);
        verifyNoInteractions(athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserByType_NullAuthentication_ThrowsIllegalStateException() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserByType(Trainer.class));
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserByType_InvalidPrincipal_ThrowsIllegalStateException() {
        // Given
        when(authentication.getPrincipal()).thenReturn("invalid");

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserByType(Trainer.class));
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verify(authentication).getPrincipal();
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserByType_TrainerNotFound_ThrowsIllegalStateException() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(trainerRepository.findByUserCredentials(userCredentials))
                .thenReturn(Optional.empty());

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserByType(Trainer.class));
        assertEquals("Trainer not found for user: john.doe@example.com", exception.getMessage());
        verify(authentication).getPrincipal();
        verify(trainerRepository).findByUserCredentials(userCredentials);
        verifyNoInteractions(gymOwnerRepository, athleteRepository);
    }

    @Test
    void getCurrentUserByType_MismatchedUserType_ThrowsIllegalArgumentException() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.ATHLETE);
        when(authentication.getPrincipal()).thenReturn(userCredentials);

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> currentUserService.getCurrentUserByType(Trainer.class));
        assertEquals("Requested user type Trainer does not match credentials type ATHLETE",
                exception.getMessage());
        verify(authentication).getPrincipal();
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserByType_UnsupportedUserType_ThrowsIllegalStateException() {
        // Given
        userCredentials.setUserType(UserCredentials.UserType.ADMIN);
        when(authentication.getPrincipal()).thenReturn(userCredentials);

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserByType(Trainer.class));
        assertEquals("Unsupported user type: ADMIN", exception.getMessage());
        verify(authentication).getPrincipal();
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserCredentials_Success() {
        // Given
        when(authentication.getPrincipal()).thenReturn(userCredentials);

        // When
        UserCredentials result = currentUserService.getCurrentUserCredentials();

        // Then
        assertNotNull(result);
        assertEquals(userCredentials, result);
        verify(authentication).getPrincipal();
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserCredentials_NullAuthentication_ThrowsIllegalStateException() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserCredentials());
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }

    @Test
    void getCurrentUserCredentials_InvalidPrincipal_ThrowsIllegalStateException() {
        // Given
        when(authentication.getPrincipal()).thenReturn("invalid");

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> currentUserService.getCurrentUserCredentials());
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verify(authentication).getPrincipal();
        verifyNoInteractions(gymOwnerRepository, athleteRepository, trainerRepository);
    }
}
