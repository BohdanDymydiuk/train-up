package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.mapper.GymMapper;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import java.util.HashSet;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class GymServiceImplTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private GymMapper gymMapper;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private GymServiceImpl gymService;

    private GymOwner gymOwner;
    private UserCredentials userCredentials;
    private Gym gym;
    private GymAddressDto addressDto;
    private GymRegistrationRequestDto registrationRequestDto;
    private GymResponseDto responseDto;
    private GymUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        gymOwner = new GymOwner();
        userCredentials = new UserCredentials();
        userCredentials.setId(1L);
        userCredentials.setEmail("owner@example.com");
        gymOwner.setUserCredentials(userCredentials);

        addressDto = new GymAddressDto(
                "Ukraine",
                "Kyiv",
                "Shevchenkivskyi",
                "Khreshchatyk",
                "1"
        );
        registrationRequestDto = new GymRegistrationRequestDto(
                "Test Gym",
                addressDto,
                new HashSet<>(List.of(1L)),
                "Test description",
                "http://testgym.com",
                new HashSet<>(List.of("+380501234567")),
                new HashSet<>(),
                new HashSet<>(List.of(1L)),
                new HashSet<>(List.of("http://photo1.jpg"))
        );

        gym = new Gym();
        gym.setId(1L);
        gym.setName("Test Gym");
        gym.setLocation(new Address());
        gym.setGymOwner(gymOwner);
        gym.setSports(new HashSet<>());
        gym.setTrainers(new HashSet<>());
        gym.setPhoneNumbers(new HashSet<>());
        gym.setPhotos(new HashSet<>());

        responseDto = new GymResponseDto(
                1L,
                "Test Gym",
                addressDto,
                new HashSet<>(List.of(1L)),
                "Test description",
                "http://testgym.com",
                new HashSet<>(List.of("+380501234567")),
                new HashSet<>(),
                new HashSet<>(List.of(1L)),
                0.0f,
                0,
                1L,
                new HashSet<>(List.of("http://photo1.jpg"))
        );

        updateRequestDto = new GymUpdateRequestDto(
                "Updated Gym",
                new GymAddressDto("Ukraine", "Lviv", null, "Rynok", "2"),
                new HashSet<>(List.of(2L)),
                "Updated description",
                "http://updatedgym.com",
                new HashSet<>(List.of("+380501234568")),
                new HashSet<>(),
                new HashSet<>(List.of(2L)),
                new HashSet<>(List.of("http://photo2.jpg"))
        );
    }

    @Test
    void save_Success() {
        // Given
        when(gymMapper.toModel(
                eq(registrationRequestDto),
                eq(gymOwner),
                any(SportRepository.class),
                any(TrainerRepository.class),
                any(AddressRepository.class))
        ).thenReturn(gym);
        when(gymRepository.save(gym)).thenReturn(gym);
        when(gymMapper.toDto(gym)).thenReturn(responseDto);

        // When
        GymResponseDto result = gymService.save(gymOwner, registrationRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(gymMapper).toModel(
                eq(registrationRequestDto),
                eq(gymOwner),
                any(SportRepository.class),
                any(TrainerRepository.class),
                any(AddressRepository.class)
        );
        verify(gymRepository).save(gym);
        verify(gymMapper).toDto(gym);
    }

    @Test
    void getAllGyms_Success() {
        // Given
        GymFilterRequestDto filter = new GymFilterRequestDto(
                "Test",
                "Ukraine",
                "Kyiv",
                null,
                null,
                null,
                null,
                null,
                null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Gym> gymPage = new PageImpl<>(List.of(gym));
        when(gymRepository.findGymsByCriteria(
                eq(filter.name()),
                eq(filter.locationCountry()),
                eq(filter.locationCity()),
                eq(filter.locationCityDistrict()),
                eq(filter.locationStreet()),
                eq(filter.locationHouse()),
                eq(filter.sportIds()),
                eq(filter.trainerIds()),
                eq(filter.overallRating()),
                eq(pageable))
        ).thenReturn(gymPage);
        when(gymMapper.toDto(gym)).thenReturn(responseDto);

        // When
        List<GymResponseDto> result = gymService.getAllGyms(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(gymRepository).findGymsByCriteria(
                eq(filter.name()),
                eq(filter.locationCountry()),
                eq(filter.locationCity()),
                eq(filter.locationCityDistrict()),
                eq(filter.locationStreet()),
                eq(filter.locationHouse()),
                eq(filter.sportIds()),
                eq(filter.trainerIds()),
                eq(filter.overallRating()),
                eq(pageable)
        );
        verify(gymMapper).toDto(gym);
    }

    @Test
    void getGymsByGymOwner_Success() {
        // Given
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userCredentials, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Gym> gymPage = new PageImpl<>(List.of(gym));
        when(currentUserService.getCurrentUserByType(GymOwner.class)).thenReturn(gymOwner);
        when(gymRepository.getGymsByGymOwner(gymOwner, pageable)).thenReturn(gymPage);
        when(gymMapper.toDto(gym)).thenReturn(responseDto);

        // When
        Page<GymResponseDto> result = gymService.getGymsByGymOwner(authentication, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(responseDto, result.getContent().get(0));
        verify(currentUserService).getCurrentUserByType(GymOwner.class);
        verify(gymRepository).getGymsByGymOwner(gymOwner, pageable);
        verify(gymMapper).toDto(gym);
    }

    @Test
    void getGymById_Success() {
        // Given
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
        when(gymMapper.toDto(gym)).thenReturn(responseDto);

        // When
        GymResponseDto result = gymService.getGymById(1L);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(gymRepository).findById(1L);
        verify(gymMapper).toDto(gym);
    }

    @Test
    void deleteGymById_Success() {
        // Given
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // When
        gymService.deleteGymById(1L);

        // Then
        verify(gymRepository).findById(1L);
        verify(gymRepository).deleteById(1L);
    }

    @Test
    void canUserModifyGym_Success() {
        // Given
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userCredentials, null);
        when(userCredentialsRepository.findByEmail("owner@example.com"))
                .thenReturn(Optional.of(userCredentials));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // When
        boolean result = gymService.canUserModifyGym(authentication, 1L);

        // Then
        assertTrue(result);
        verify(userCredentialsRepository).findByEmail("owner@example.com");
        verify(gymRepository).findById(1L);
    }

    @Test
    void updateGym_Success() {
        // Given
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(new Sport()));
        when(trainerRepository.findById(2L)).thenReturn(Optional.of(new Trainer()));
        when(addressRepository.save(any(Address.class))).thenReturn(gym.getLocation());
        when(gymRepository.save(gym)).thenReturn(gym);
        when(gymMapper.toDto(gym)).thenReturn(responseDto);

        // When
        GymResponseDto result = gymService.updateGym(1L, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(gymRepository).findById(1L);
        verify(gymRepository).save(gym);
        verify(gymMapper).toDto(gym);
    }
}
