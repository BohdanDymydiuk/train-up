package com.example.trainup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.exception.PhotoUploadException;
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
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GymServiceImpl implements GymService {
    private static final String CANNOT_FIND_GYM_MSG = "Cannot find Gym by id: ";

    private final GymRepository gymRepository;
    private final GymMapper gymMapper;
    private final SportRepository sportRepository;
    private final TrainerRepository trainerRepository;
    private final AddressRepository addressRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final CurrentUserService currentUserService;
    private final Optional<Cloudinary> cloudinary;

    private GymService self;

    @Autowired
    public void setSelf(@Lazy GymService self) {
        this.self = self;
    }

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
        Set<Long> sportIds = (filter.sportIds() == null || filter.sportIds().isEmpty())
                ? null : filter.sportIds();
        Set<Long> trainerIds = (filter.trainerIds() == null || filter.trainerIds().isEmpty())
                ? null : filter.trainerIds();

        Page<Gym> gymPage = gymRepository.findGymsByCriteria(
                filter.name(),
                filter.locationCountry(),
                filter.locationCity(),
                filter.locationCityDistrict(),
                filter.locationStreet(),
                filter.locationHouse(),
                sportIds,
                trainerIds,
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

        return gymsByGymOwner.map(gymMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GymResponseDto getGymById(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Gym by id:" + id));
        return gymMapper.toDto(gym);
    }

    @Override
    public void deleteGymById(Long id) {
        gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CANNOT_FIND_GYM_MSG + id));
        gymRepository.deleteById(id);
        log.debug("Deleted gym with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserModifyGym(Authentication authentication, Long gymId) {
        if (authentication == null || authentication.getName() == null || gymId == null) {
            log.debug("Received authentication: {}, gymId: {}", authentication, gymId);
            log.debug("Authentication is null or email is null or gymId is null, access denied.");
            return false;
        }
        String email = authentication.getName(); // Тепер витягуємо ім'я з Authentication
        log.debug("Received email: {}, gymId: {}", email, gymId);

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
                .orElseThrow(() -> new EntityNotFoundException(CANNOT_FIND_GYM_MSG + id));

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

    @Override
    public String uploadGymPhoto(Long id, MultipartFile file, Authentication authentication) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CANNOT_FIND_GYM_MSG + id));

        if (!self.canUserModifyGym(authentication, id)) {
            throw new AccessDeniedException("You do not have permission to modify this gym.");
        }

        if (gym.getPhotos().size() >= 5) {
            throw new ConstraintViolationException("Maximum number of photos (5) exceeded", null);
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty or missing");
        }

        if (cloudinary.isEmpty()) {
            throw new IllegalStateException("Cloudinary service is not configured or available.");
        }

        Cloudinary actualCloudinary = cloudinary.get();

        try {
            Map<String, Object> uploadResult = actualCloudinary
                    .uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            GymPhoto gymPhoto = new GymPhoto();
            gymPhoto.setImageUrl(imageUrl);
            gym.addPhoto(gymPhoto);
            gymRepository.save(gym);

            return imageUrl;
        } catch (IOException e) {
            throw new PhotoUploadException("Failed to upload photo to Cloudinary: "
                    + e.getMessage(), e);
        }
    }
}
