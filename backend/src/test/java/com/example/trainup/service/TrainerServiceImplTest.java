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

import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import com.example.trainup.mapper.TrainerMapper;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import com.example.trainup.service.users.TrainerServiceImpl;
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

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserCredentialService userCredentialService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private TrainerRegistrationRequestDto registrationRequestDto;
    private TrainerResponseDto trainerResponseDto;
    private TrainerUpdateRequestDto updateRequestDto;
    private TrainerFilterRequestDto filterRequestDto;
    private Trainer trainer;
    private UserCredentials userCredentials;
    private Address address;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new TrainerRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1985, 5, 15),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "Password123!",
                "Password123!",
                Set.of("123456789"),
                Set.of(1L),
                Set.of(1L),
                new TrainerAddressDto("USA", "New York", "Downtown", "Main St", "123"),
                true,
                List.of("Certificate1"),
                "Experienced trainer",
                "http://social.com"
        );

        trainerResponseDto = new TrainerResponseDto(
                1L,
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1985, 5, 15),
                "http://example.com/image.jpg",
                "john.doe@example.com",
                "TRAINER",
                Set.of("123456789"),
                Set.of(1L),
                Set.of(1L),
                new TrainerAddressDto("USA", "New York", "Downtown", "Main St", "123"),
                "Experienced trainer",
                "http://social.com",
                4.5f,
                10
        );

        updateRequestDto = new TrainerUpdateRequestDto(
                "Jane",
                "Doe",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 15),
                "http://example.com/new-image.jpg",
                Set.of("987654321"),
                Set.of(2L),
                Set.of(2L),
                new TrainerAddressDto("USA", "Boston", "Uptown", "Elm St", "456"),
                false,
                List.of("Certificate2"),
                "Updated description",
                "http://new-social.com"
        );

        filterRequestDto = new TrainerFilterRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                Set.of(1L),
                Set.of(1L),
                "USA",
                "New York",
                "Downtown",
                "Main St",
                "123",
                true
        );

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setGender(Gender.MALE);
        trainer.setDateOfBirth(LocalDate.of(1985, 5, 15));
        trainer.setProfileImageUrl("http://example.com/image.jpg");
        trainer.setPhoneNumbers(Set.of("123456789"));
        trainer.setSports(Set.of(new Sport()));
        trainer.setGyms(Set.of(new Gym()));
        trainer.setOnlineTraining(true);
        trainer.setCertificates(Set.of("Certificate1"));
        trainer.setDescription("Experienced trainer");
        trainer.setSocialMediaLinks("http://social.com");
        trainer.setOverallRating(4.5f);
        trainer.setNumberOfReviews(10);

        userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        trainer.setUserCredentials(userCredentials);

        address = new Address();
        address.setCountry("USA");
        address.setCity("New York");
        address.setCityDistrict("Downtown");
        address.setStreet("Main St");
        address.setHouse("123");
        trainer.setLocation(address);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void register_Success() {
        // Given
        when(trainerMapper.toModel(
                eq(registrationRequestDto),
                any(PasswordEncoder.class),
                any(SportRepository.class),
                any(GymRepository.class),
                any(AddressRepository.class))
        ).thenReturn(trainer);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(trainerMapper.toDto(trainer)).thenReturn(trainerResponseDto);

        // When
        TrainerResponseDto result = trainerService.register(registrationRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(trainerResponseDto, result);
        verify(trainerMapper).toModel(eq(registrationRequestDto), any(PasswordEncoder.class),
                any(SportRepository.class), any(GymRepository.class), any(AddressRepository.class));
        verify(trainerRepository).save(trainer);
        verify(userCredentialService).assignRoleBasedOnUserType(userCredentials);
        verify(trainerMapper).toDto(trainer);
    }

    @Test
    void getAllTrainers_Success() {
        // Given
        Page<Trainer> trainerPage = new PageImpl<>(List.of(trainer), pageable, 1);
        when(trainerRepository.findTrainersByCriteria(
                eq("John"), eq("Doe"), eq(Gender.MALE), eq(Set.of(1L)), eq(Set.of(1L)),
                eq("USA"), eq("New York"), eq("Downtown"), eq("Main St"), eq("123"),
                eq(true), eq(pageable)))
                .thenReturn(trainerPage);
        when(trainerMapper.toDto(trainer)).thenReturn(trainerResponseDto);

        // When
        List<TrainerResponseDto> result = trainerService
                .getAllTrainers(filterRequestDto, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainerResponseDto, result.get(0));
        verify(trainerRepository).findTrainersByCriteria(
                eq("John"), eq("Doe"), eq(Gender.MALE), eq(Set.of(1L)), eq(Set.of(1L)),
                eq("USA"), eq("New York"), eq("Downtown"), eq("Main St"), eq("123"),
                eq(true), eq(pageable));
        verify(trainerMapper).toDto(trainer);
    }

    @Test
    void getTrainerByAuth_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(currentUserService.getCurrentUserByType(Trainer.class)).thenReturn(trainer);
        when(trainerMapper.toDto(trainer)).thenReturn(trainerResponseDto);

        // When
        TrainerResponseDto result = trainerService.getTrainerByAuth(authentication);

        // Then
        assertNotNull(result);
        assertEquals(trainerResponseDto, result);
        verify(authentication).getPrincipal();
        verify(currentUserService).getCurrentUserByType(Trainer.class);
        verify(trainerMapper).toDto(trainer);
    }

    @Test
    void getTrainerByAuth_NullAuthentication_ThrowsIllegalStateException() {
        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> trainerService.getTrainerByAuth(null));
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verifyNoInteractions(currentUserService, trainerMapper);
    }

    @Test
    void getTrainerByAuth_InvalidPrincipal_ThrowsIllegalStateException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("invalid");

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> trainerService.getTrainerByAuth(authentication));
        assertEquals("User is not authenticated or principal is not UserCredentials",
                exception.getMessage());
        verify(authentication).getPrincipal();
        verifyNoInteractions(currentUserService, trainerMapper);
    }

    @Test
    void getTrainerById_Success() {
        // Given
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(trainerResponseDto);

        // When
        TrainerResponseDto result = trainerService.getTrainerById(1L);

        // Then
        assertNotNull(result);
        assertEquals(trainerResponseDto, result);
        verify(trainerRepository).findById(1L);
        verify(trainerMapper).toDto(trainer);
    }

    @Test
    void getTrainerById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        when(trainerRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainerService.getTrainerById(999L));
        assertEquals("Cannot find Trainer by id:999", exception.getMessage());
        verify(trainerRepository).findById(999L);
        verifyNoInteractions(trainerMapper);
    }

    @Test
    void canUserModifyTrainer_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        // When
        boolean result = trainerService.canUserModifyTrainer(authentication, 1L);

        // Then
        assertTrue(result);
        verify(authentication).getName();
        verify(trainerRepository).findById(1L);
    }

    @Test
    void canUserModifyTrainer_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainerService.canUserModifyTrainer(authentication, 999L));
        assertEquals("Can not find Trainer by id:999", exception.getMessage());
        verify(authentication).getName();
        verify(trainerRepository).findById(999L);
    }

    @Test
    void canUserModifyTrainer_Unauthorized_ReturnsFalse() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("other@example.com");
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        // When
        boolean result = trainerService.canUserModifyTrainer(authentication, 1L);

        // Then
        assertFalse(result);
        verify(authentication).getName();
        verify(trainerRepository).findById(1L);
    }

    @Test
    void canUserModifyTrainer_NullAuthentication_ReturnsFalse() {
        // When
        boolean result = trainerService.canUserModifyTrainer(null, 1L);

        // Then
        assertFalse(result);
        verifyNoInteractions(trainerRepository);
    }

    @Test
    void deleteTrainerById_Success() {
        // Given
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        doNothing().when(trainerRepository).deleteById(1L);

        // When
        trainerService.deleteTrainerById(1L);

        // Then
        verify(trainerRepository).findById(1L);
        verify(trainerRepository).deleteById(1L);
    }

    @Test
    void deleteTrainerById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        when(trainerRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainerService.deleteTrainerById(999L));
        assertEquals("Cannot find Trainer by id:999", exception.getMessage());
        verify(trainerRepository).findById(999L);
        verify(trainerRepository, never()).deleteById(any());
    }

    @Test
    void updateTrainerByAuth_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(trainer));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(new Sport()));
        when(gymRepository.findById(2L)).thenReturn(Optional.of(new Gym()));
        when(addressRepository.findByCountryAndCityAndStreetAndHouse(
                eq("USA"), eq("Boston"), eq("Elm St"), eq("456")))
                .thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(trainerMapper.toDto(trainer)).thenReturn(trainerResponseDto);

        // When
        TrainerResponseDto result = trainerService
                .updateTrainerByAuth(authentication, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(trainerResponseDto, result);
        verify(authentication, times(2)).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(2L);
        verify(gymRepository).findById(2L);
        verify(addressRepository).findByCountryAndCityAndStreetAndHouse(
                eq("USA"), eq("Boston"), eq("Elm St"), eq("456"));
        verify(trainerRepository).save(any(Trainer.class));
        verify(trainerMapper).toDto(trainer);
    }

    @Test
    void updateTrainerByAuth_NullAuthentication_ThrowsIllegalStateException() {
        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> trainerService.updateTrainerByAuth(null, updateRequestDto));
        assertEquals("User is not authenticated", exception.getMessage());
        verifyNoInteractions(trainerRepository, sportRepository, gymRepository, addressRepository,
                trainerMapper);
    }

    @Test
    void updateTrainerByAuth_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        // When/Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainerService.updateTrainerByAuth(authentication, updateRequestDto));
        assertEquals("Trainer not found for email: john.doe@example.com", exception.getMessage());
        verify(authentication, times(2)).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verifyNoInteractions(sportRepository, gymRepository, addressRepository, trainerMapper);
    }

    @Test
    void updateTrainerByAuth_InvalidSportId_ThrowsIllegalArgumentException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(trainer));
        when(sportRepository.findById(2L)).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainerService.updateTrainerByAuth(authentication, updateRequestDto));
        assertEquals("Sport with id 2 not found", exception.getMessage());
        verify(authentication, times(2)).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(2L);
        verifyNoMoreInteractions(trainerRepository, gymRepository, addressRepository,
                trainerMapper);
    }

    @Test
    void updateTrainerByAuth_InvalidGymId_ThrowsIllegalArgumentException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@example.com");
        when(trainerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(trainer));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(new Sport()));
        when(gymRepository.findById(2L)).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainerService.updateTrainerByAuth(authentication, updateRequestDto));
        assertEquals("Gym with id 2 not found", exception.getMessage());
        verify(authentication, times(2)).getName();
        verify(trainerRepository).findByEmail("john.doe@example.com");
        verify(sportRepository).findById(2L);
        verify(gymRepository).findById(2L);
        verifyNoMoreInteractions(trainerRepository, addressRepository, trainerMapper);
    }
}
