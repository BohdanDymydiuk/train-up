package com.example.trainup.service;

import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.mapper.GymMapper;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.GymPhoto;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GymServiceImpl implements GymService {
    private final GymRepository gymRepository;
    private final GymMapper gymMapper;
    private final SportRepository sportRepository;
    private final TrainerRepository trainerRepository;
    private final AddressRepository addressRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final CurrentUserService currentUserService;

    @Override
    public GymResponseDto save(GymOwner gymOwner, GymRegistrationRequestDto requestDto) {
        Gym gym = gymMapper.toModel(
                requestDto,
                gymOwner,
                sportRepository,
                trainerRepository,
                addressRepository
        );
        Gym savedGym = gymRepository.save(gym);
        return gymMapper.toDto(savedGym);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymResponseDto> getAllGyms(GymFilterRequestDto filter, Pageable pageable) {
        log.debug("Fetching gyms with filter: {}", filter);
        log.debug("Name: {}, Country: {}, City: {}, District: {}, Street: {}, House: {}, "
                        + "SportIds: {}, TrainerIds: {}, Rating: {}",
                filter.name(), filter.locationCountry(), filter.locationCity(),
                filter.locationCityDistrict(), filter.locationStreet(), filter.locationHouse(),
                filter.sportIds(), filter.trainerIds(), filter.overallRating());
        Page<Gym> gymPage = gymRepository.findGymsByCriteria(
                filter.name(),
                filter.locationCountry(),
                filter.locationCity(),
                filter.locationCityDistrict(),
                filter.locationStreet(),
                filter.locationHouse(),
                filter.sportIds(),
                filter.trainerIds(),
                filter.overallRating(),
                pageable
        );

        log.debug("Found {} gyms", gymPage.getTotalElements());
        return gymPage.stream()
                .map(gymMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GymResponseDto> getGymsByGymOwner(Authentication authentication,
                                                  Pageable pageable) {
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserCredentials)) {
            throw new IllegalStateException("User is not authenticated "
                    + "or principal is not UserCredentials");
        }
        GymOwner gymOwner = currentUserService.getCurrentUserByType(GymOwner.class);

        Page<Gym> gymsByGymOwner = gymRepository.getGymsByGymOwner(gymOwner, pageable);

        Page<GymResponseDto> gymsByGymOwnerDto = gymsByGymOwner.map(gymMapper::toDto);
        return gymsByGymOwnerDto;
    }

    @Override
    @Transactional(readOnly = true)
    public GymResponseDto getGymById(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Gym by id:" + id));
        GymResponseDto dto = gymMapper.toDto(gym);
        return dto;
    }

    @Override
    public void deleteGymById(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Gym by id: " + id));
        gymRepository.deleteById(id);
        log.debug("Deleted gym with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserModifyGym(String email, Long gymId) {
        log.debug("Received email: {}, gymId: {}", email, gymId);
        if (email == null) {
            log.debug("Email is null, access denied");
            return false;
        }

        Optional<UserCredentials> userOptional = userCredentialsRepository
                .findByEmail(email);
        if (userOptional.isEmpty()) {
            log.debug("User with email {} not found", email);
            return false;
        }

        UserCredentials user = userOptional.get();

        Optional<Gym> gymOptional = gymRepository.findById(gymId);
        if (gymOptional.isEmpty()) {
            log.debug("Gym with ID {} not found", gymId);
            return false;
        }

        Gym gym = gymOptional.get();
        boolean isOwner = gym.getGymOwner() != null
                && gym.getGymOwner().getUserCredentials() != null
                && gym.getGymOwner().getUserCredentials().getId().equals(user.getId());

        log.debug("User ID: {}, Gym Owner ID: {}, Access granted: {}",
                user.getId(),
                gym.getGymOwner() != null && gym.getGymOwner()
                        .getUserCredentials() != null
                        ? gym.getGymOwner().getUserCredentials().getId() : "N/A",
                isOwner);

        return isOwner;
    }

    @Override
    public GymResponseDto updateGym(Long id, GymUpdateRequestDto requestDto) {
        Gym existingGym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Gym by id: " + id));

        Optional.ofNullable(requestDto.name()).ifPresent(existingGym::setName);
        Optional.ofNullable(requestDto.description()).ifPresent(existingGym::setDescription);
        Optional.ofNullable(requestDto.website()).ifPresent(existingGym::setWebsite);
        Optional.ofNullable(requestDto.phoneNumbers()).ifPresent(phoneNumbers ->
                existingGym.setPhoneNumbers(new HashSet<>(phoneNumbers)));
        Optional.ofNullable(requestDto.workingHours()).ifPresent(workingHours ->
                existingGym.setWorkingHours(new HashSet<>(workingHours)));

        updateLocation(existingGym, requestDto.location());
        updateEntities(existingGym::setSports, requestDto.sportIds(), sportRepository::findById,
                "Sport with id ");
        updateEntities(existingGym::setTrainers, requestDto.trainerIds(),
                trainerRepository::findById,"Trainer with id ");
        updatePhotos(existingGym, requestDto.photoUrls());

        Gym updatedGym = gymRepository.save(existingGym);
        return gymMapper.toDto(updatedGym);
    }

    private void updateLocation(Gym gym, GymAddressDto locationDto) {
        if (locationDto != null) {
            Address address = gym.getLocation();
            if (address == null) {
                throw new IllegalStateException("Gym with ID " + gym.getId()
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

    private <T> void updateEntities(Consumer<Set<T>> setter, Set<Long> ids,
                                    Function<Long, Optional<T>> finder, String errorPrefix) {
        if (ids != null) {
            Set<T> entities = ids.stream()
                    .map(id -> finder.apply(id)
                            .orElseThrow(() -> new IllegalArgumentException(errorPrefix + id
                                    + " not found")))
                    .collect(Collectors.toSet());
            if (entities.isEmpty()) {
                throw new IllegalArgumentException("At least one " + errorPrefix.toLowerCase()
                        .replace(" with id ", "") + " must be associated.");
            }
            setter.accept(entities);
        }

    }

    private void updatePhotos(Gym gym, Set<String> photoUrls) {
        Optional.ofNullable(photoUrls).ifPresent(urls -> {
            Set<String> newPhotoUrls = new HashSet<>(urls);
            Set<GymPhoto> currentPhotos = new HashSet<>(gym.getPhotos());

            currentPhotos.removeIf(photo -> !newPhotoUrls.contains(photo.getImageUrl()));
            newPhotoUrls.stream()
                    .filter(url -> currentPhotos.stream()
                            .noneMatch(photo -> photo.getImageUrl().equals(url)))
                    .map(url -> {
                        GymPhoto newPhoto = new GymPhoto();
                        newPhoto.setImageUrl(url);
                        newPhoto.setGym(gym);
                        return newPhoto;
                    })
                    .forEach(currentPhotos::add);

            if (currentPhotos.size() > 5) {
                throw new ConstraintViolationException("Maximum number of photos (5) exceeded.",
                        null);
            }

            gym.setPhotos(currentPhotos);
        });
    }
}
