package com.example.trainup.mapper;

import static com.example.trainup.model.WorkingHoursEntry.DayOfTheWeek.MONDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.GymPhoto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.WorkingHoursEntry;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

class GymMapperTest {

    private GymMapper gymMapper;

    private SportRepository sportRepository;
    private TrainerRepository trainerRepository;
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        sportRepository = Mockito.mock(SportRepository.class);
        trainerRepository = Mockito.mock(TrainerRepository.class);
        addressRepository = Mockito.mock(AddressRepository.class);

        gymMapper = Mappers.getMapper(GymMapper.class);
    }

    @Test
    void toModel_Success() {
        // Given
        GymOwner gymOwner = new GymOwner();
        gymOwner.setId(1L);

        Sport sport1 = new Sport();
        sport1.setId(1L);
        sport1.setSportName("Football");
        Sport sport2 = new Sport();
        sport2.setId(2L);
        sport2.setSportName("Tennis");
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport1));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(sport2));

        Trainer trainer1 = new Trainer();
        trainer1.setId(3L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(4L);
        when(trainerRepository.findById(3L)).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findById(4L)).thenReturn(Optional.of(trainer2));

        Address address = new Address();
        address.setCountry("Ukraine");
        address.setCity("Kyiv");
        address.setCityDistrict("Shevchenkivskyi");
        address.setStreet("Shevchenka");
        address.setHouse("10");
        when(addressRepository.findByCountryAndCityAndStreetAndHouse(
                "Ukraine",
                "Kyiv",
                "Shevchenka",
                "10")
        ).thenReturn(Optional.of(address));

        GymRegistrationRequestDto requestDto = new GymRegistrationRequestDto(
                "Fitness Center",
                new GymAddressDto("Ukraine", "Kyiv", "Shevchenkivskyi", "Shevchenka", "10"),
                Set.of(1L, 2L),
                "A great gym",
                "https://fitness.com",
                Set.of("+380501234567", "+380509876543"),
                Set.of(new WorkingHoursEntry(MONDAY, LocalTime.of(8, 0), LocalTime.of(20, 0))),
                Set.of(3L, 4L),
                Set.of("http://photo1.jpg", "http://photo2.jpg")
        );

        // When
        Gym gym = gymMapper.toModel(
                requestDto,
                gymOwner,
                sportRepository,
                trainerRepository,
                addressRepository
        );

        // Then
        assertNotNull(gym);
        assertNull(gym.getId());
        assertEquals("Fitness Center", gym.getName());
        assertEquals(address, gym.getLocation());
        assertEquals(2, gym.getSports().size());
        assertTrue(gym.getSports().contains(sport1));
        assertTrue(gym.getSports().contains(sport2));
        assertEquals("A great gym", gym.getDescription());
        assertEquals("https://fitness.com", gym.getWebsite());
        assertEquals(Set.of("+380501234567", "+380509876543"), gym.getPhoneNumbers());
        assertEquals(1, gym.getWorkingHours().size());
        assertEquals(2, gym.getTrainers().size());
        assertTrue(gym.getTrainers().contains(trainer1));
        assertTrue(gym.getTrainers().contains(trainer2));
        assertEquals(2, gym.getPhotos().size());
        assertEquals(gymOwner, gym.getGymOwner());
        assertEquals(0.0f, gym.getOverallRating());
        assertEquals(0, gym.getNumberOfReviews());
    }

    @Test
    void toModel_WithNewAddress_Success() {
        // Given
        GymOwner gymOwner = new GymOwner();
        gymOwner.setId(1L);

        Sport sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Football");
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));

        Trainer trainer = new Trainer();
        trainer.setId(3L);
        when(trainerRepository.findById(3L)).thenReturn(Optional.of(trainer));

        Address newAddress = new Address();
        newAddress.setCountry("Ukraine");
        newAddress.setCity("Lviv");
        newAddress.setStreet("Sichovykh Striltsiv");
        newAddress.setHouse("5");
        when(addressRepository.findByCountryAndCityAndStreetAndHouse(
                "Ukraine",
                "Lviv",
                "Sichovykh Striltsiv",
                "5")
        ).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        GymRegistrationRequestDto requestDto = new GymRegistrationRequestDto(
                "New Gym",
                new GymAddressDto("Ukraine", "Lviv", null,"Sichovykh Striltsiv", "5"),
                Set.of(1L),
                null,
                null,
                Set.of("+380501234567"),
                null,
                Set.of(3L),
                Set.of("http://photo1.jpg")
        );

        // When
        Gym gym = gymMapper.toModel(
                requestDto,
                gymOwner,
                sportRepository,
                trainerRepository,
                addressRepository
        );

        // Then
        assertNotNull(gym);
        assertNull(gym.getId());
        assertEquals("New Gym", gym.getName());
        assertEquals(newAddress, gym.getLocation());
        assertEquals(1, gym.getSports().size());
        assertTrue(gym.getSports().contains(sport));
        assertEquals(Set.of("+380501234567"), gym.getPhoneNumbers());
        assertEquals(1, gym.getTrainers().size());
        assertTrue(gym.getTrainers().contains(trainer));
        assertEquals(1, gym.getPhotos().size());
        assertEquals(gymOwner, gym.getGymOwner());
    }

    @Test
    void toDto_Success() {
        // Given
        Gym gym = new Gym();
        gym.setId(1L);
        gym.setName("Fitness Center");
        Address address = new Address();
        address.setCountry("Ukraine");
        address.setCity("Kyiv");
        address.setStreet("Shevchenka");
        address.setHouse("10");
        gym.setLocation(address);
        Set<Sport> sports = new HashSet<>();
        Sport sport = new Sport();
        sport.setId(1L);
        sports.add(sport);
        gym.setSports(sports);
        gym.setPhoneNumbers(new HashSet<>(Set.of("+380501234567")));
        Set<Trainer> trainers = new HashSet<>();
        Trainer trainer = new Trainer();
        trainer.setId(3L);
        trainers.add(trainer);
        gym.setTrainers(trainers);
        gym.setOverallRating(4.5f);
        gym.setNumberOfReviews(10);
        GymOwner gymOwner = new GymOwner();
        gymOwner.setId(2L);
        gym.setGymOwner(gymOwner);
        Set<GymPhoto> photos = new HashSet<>();
        GymPhoto photo = new GymPhoto();
        photo.setImageUrl("http://photo1.jpg");
        photos.add(photo);
        gym.setPhotos(photos);

        // When
        GymResponseDto result = gymMapper.toDto(gym);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Fitness Center", result.name());
        assertEquals(new GymAddressDto("Ukraine", "Kyiv", null, "Shevchenka", "10"),
                result.location());
        assertEquals(Set.of(1L), result.sportIds());
        assertEquals(Set.of("+380501234567"), result.phoneNumbers());
        assertEquals(Set.of(3L), result.trainerIds());
        assertEquals(4.5f, result.overallRating());
        assertEquals(10, result.numberOfReviews());
        assertEquals(2L, result.gymOwnerId());
        assertEquals(Set.of("http://photo1.jpg"), result.photoUrls());
    }
}
