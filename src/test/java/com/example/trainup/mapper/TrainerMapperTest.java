package com.example.trainup.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class TrainerMapperTest {

    private TrainerMapper trainerMapper;

    private SportRepository sportRepository;
    private GymRepository gymRepository;
    private AddressRepository addressRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        sportRepository = Mockito.mock(SportRepository.class);
        gymRepository = Mockito.mock(GymRepository.class);
        addressRepository = Mockito.mock(AddressRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        trainerMapper = Mappers.getMapper(TrainerMapper.class);
    }

    @Test
    void toDto_Success() {
        // Given
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setGender(Gender.MALE);
        trainer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainer.setProfileImageUrl("http://image.jpg");
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        trainer.setUserCredentials(userCredentials);
        trainer.setPhoneNumbers(new HashSet<>(Set.of("+380501234567")));
        Set<Sport> sports = new HashSet<>();
        Sport sport = new Sport();
        sport.setId(1L);
        sports.add(sport);
        trainer.setSports(sports);
        Set<Gym> gyms = new HashSet<>();
        Gym gym = new Gym();
        gym.setId(2L);
        gyms.add(gym);
        trainer.setGyms(gyms);
        Address address = new Address();
        address.setCountry("Ukraine");
        address.setCity("Kyiv");
        address.setStreet("Shevchenka");
        address.setHouse("10");
        trainer.setLocation(address);
        trainer.setDescription("Experienced trainer");
        trainer.setSocialMediaLinks("https://social.com");
        trainer.setOverallRating(4.5f);
        trainer.setNumberOfReviews(10);

        // When
        TrainerResponseDto result = trainerMapper.toDto(trainer);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals(Gender.MALE, result.gender());
        assertEquals(LocalDate.of(1990, 1, 1), result.dateOfBirth());
        assertEquals("http://image.jpg", result.profileImageUrl());
        assertEquals("john.doe@example.com", result.email());
        assertEquals("TRAINER", result.userType());
        assertEquals(Set.of("+380501234567"), result.phoneNumbers());
        assertEquals(Set.of(1L), result.sportIds());
        assertEquals(Set.of(2L), result.gymIds());
        assertEquals(new TrainerAddressDto("Ukraine", "Kyiv", null, "Shevchenka", "10"),
                result.location());
        assertEquals("Experienced trainer", result.description());
        assertEquals("https://social.com", result.socialMediaLinks());
        assertEquals(4.5f, result.overallRating());
        assertEquals(10, result.numberOfReviews());
    }

    @Test
    void toModel_Success() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));

        Gym gym = new Gym();
        gym.setId(2L);
        when(gymRepository.findById(2L)).thenReturn(Optional.of(gym));

        Address address = new Address();
        address.setCountry("Ukraine");
        address.setCity("Kyiv");
        address.setStreet("Shevchenka");
        address.setHouse("10");
        when(addressRepository.findByCountryAndCityAndStreetAndHouse(
                "Ukraine",
                "Kyiv",
                "Shevchenka",
                "10")
        ).thenReturn(Optional.of(address));

        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
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
                Set.of(2L),
                new TrainerAddressDto("Ukraine", "Kyiv", "Shevchenkivskyi", "Shevchenka", "10"),
                true,
                List.of("Cert1", "Cert2"),
                "Experienced trainer",
                "https://social.com"
        );

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // When
        Trainer trainer = trainerMapper.toModel(
                requestDto,
                passwordEncoder,
                sportRepository,
                gymRepository,
                addressRepository
        );

        // Then
        assertNotNull(trainer);
        assertNull(trainer.getId());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals(Gender.MALE, trainer.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), trainer.getDateOfBirth());
        assertEquals("http://image.jpg", trainer.getProfileImageUrl());
        assertEquals(Set.of("+380501234567"), trainer.getPhoneNumbers());
        assertEquals(1, trainer.getSports().size());
        assertTrue(trainer.getSports().contains(sport));
        assertEquals(1, trainer.getGyms().size());
        assertTrue(trainer.getGyms().contains(gym));
        assertEquals(address, trainer.getLocation());
        assertTrue(trainer.getOnlineTraining());
        assertEquals(Set.of("Cert1", "Cert2"), trainer.getCertificates());
        assertEquals("Experienced trainer", trainer.getDescription());
        assertEquals("https://social.com", trainer.getSocialMediaLinks());
        assertNotNull(trainer.getUserCredentials());
        assertEquals("john.doe@example.com", trainer.getUserCredentials().getEmail());
        assertEquals("encodedPassword", trainer.getUserCredentials().getPassword());
        assertEquals(UserCredentials.UserType.TRAINER, trainer.getUserCredentials().getUserType());
    }

    @Test
    void toModel_WithNewAddress_Success() {
        // Given
        Sport sport = new Sport();
        sport.setId(3L);
        when(sportRepository.findById(3L)).thenReturn(Optional.of(sport));

        Gym gym = new Gym();
        gym.setId(4L);
        when(gymRepository.findById(4L)).thenReturn(Optional.of(gym));

        Address newAddress = new Address();
        newAddress.setCountry("Poland");
        newAddress.setCity("Warsaw");
        newAddress.setStreet("Marszalkowska");
        newAddress.setHouse("1");
        when(addressRepository.findByCountryAndCityAndStreetAndHouse(
                "Poland",
                "Warsaw",
                "Marszalkowska",
                "1")
        ).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
                "Jane",
                "Smith",
                Gender.FEMALE,
                LocalDate.of(1995, 5, 15),
                null,
                "jane.smith@example.com",
                "pass456",
                "pass456",
                Set.of("+380509876543"),
                Set.of(3L),
                Set.of(4L),
                new TrainerAddressDto("Poland", "Warsaw", null, "Marszalkowska", "1"),
                false,
                null,
                null,
                null
        );

        // When
        Trainer trainer = trainerMapper.toModel(
                requestDto,
                passwordEncoder,
                sportRepository,
                gymRepository,
                addressRepository
        );

        // Then
        assertNotNull(trainer);
        assertNull(trainer.getId());
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals(Gender.FEMALE, trainer.getGender());
        assertEquals(LocalDate.of(1995, 5, 15), trainer.getDateOfBirth());
        assertEquals(Set.of("+380509876543"), trainer.getPhoneNumbers());
        assertEquals(1, trainer.getSports().size());
        assertTrue(trainer.getSports().contains(sport));
        assertEquals(1, trainer.getGyms().size());
        assertTrue(trainer.getGyms().contains(gym));
        assertEquals(newAddress, trainer.getLocation());
        assertFalse(trainer.getOnlineTraining());
        assertTrue(trainer.getCertificates().isEmpty());
        assertNull(trainer.getDescription());
        assertNull(trainer.getSocialMediaLinks());
        assertNotNull(trainer.getUserCredentials());
        assertEquals("jane.smith@example.com", trainer.getUserCredentials().getEmail());
        assertEquals("encodedPassword", trainer.getUserCredentials().getPassword());
        assertEquals(UserCredentials.UserType.TRAINER, trainer.getUserCredentials().getUserType());
    }

    @Test
    void toModel_WithNonExistentSportId_ThrowsIllegalArgumentException() {
        // Given
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                null,
                "john.doe@example.com",
                "password123",
                "password123",
                Set.of("+380501234567"),
                Set.of(999L),
                Set.of(),
                new TrainerAddressDto("Ukraine", "Kyiv", null, "Shevchenka", "10"),
                true,
                null,
                null,
                null
        );
        when(sportRepository.findById(999L)).thenReturn(Optional.empty());

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> trainerMapper.toModel(
                        requestDto,
                        passwordEncoder,
                        sportRepository,
                        gymRepository,
                        addressRepository)
        );
    }
}
