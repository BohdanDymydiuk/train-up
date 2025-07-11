package com.example.trainup.service.users;

import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import com.example.trainup.mapper.TrainerMapper;
import com.example.trainup.model.Address;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.UserCredentialService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final PasswordEncoder encoder;
    private final SportRepository sportRepository;
    private final GymRepository gymRepository;
    private final AddressRepository addressRepository;
    private final UserCredentialService userCredentialService;
    private final CurrentUserService currentUserService;
    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public TrainerResponseDto register(TrainerRegistrationRequestDto requestDto) {
        Trainer trainer = trainerMapper
                .toModel(requestDto, encoder, sportRepository, gymRepository, addressRepository);
        trainerRepository.save(trainer);
        userCredentialService.assignRoleBasedOnUserType(trainer.getUserCredentials());
        return trainerMapper.toDto(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerResponseDto> getAllTrainers(TrainerFilterRequestDto filter,
                                                   Pageable pageable) {
        log.debug("Fetching trainers with filter: {}", filter);
        log.debug("FirstName: {}, LastName: {}, Gender: {}, SportIds: {}, GymIds: {}, "
                        + "LocationCountry: {}, LocationCity: {}, LocationCityDistrict: {}, "
                        + "LocationStreet: {}, LocationHouse: {}, OnlineTraining: {}",
                filter.firstName(), filter.lastName(), filter.gender(), filter.sportIds(),
                filter.gymIds(), filter.locationCountry(), filter.locationCity(),
                filter.locationCityDistrict(), filter.locationStreet(), filter.locationHouse(),
                filter.onlineTraining());

        Set<Long> sportIds = (filter.sportIds() == null || filter.sportIds().isEmpty())
                ? null : filter.sportIds();
        Set<Long> gymIds = (filter.gymIds() == null || filter.gymIds().isEmpty())
                ? null : filter.gymIds();

        Page<Trainer> trainerPage = trainerRepository.findTrainersByCriteria(
                filter.firstName(),
                filter.lastName(),
                filter.gender(),
                sportIds,
                gymIds,
                filter.locationCountry(),
                filter.locationCity(),
                filter.locationCityDistrict(),
                filter.locationStreet(),
                filter.locationHouse(),
                filter.onlineTraining(),
                pageable
        );

        log.debug("Found {} trainers", trainerPage.getTotalElements());
        return trainerPage.stream()
                .map(trainerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponseDto getTrainerByAuth(Authentication authentication) {
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserCredentials)) {
            throw new IllegalStateException("User is not authenticated "
                    + "or principal is not UserCredentials");
        }
        Trainer trainer = currentUserService.getCurrentUserByType(Trainer.class);
        TrainerResponseDto dto = trainerMapper.toDto(trainer);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponseDto getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Trainer by id:" + id));

        TrainerResponseDto dto = trainerMapper.toDto(trainer);
        return dto;
    }

    @Override
    public boolean canUserModifyTrainer(Authentication auth, Long trainerId) {
        if (auth == null || trainerId == null) {
            return false;
        }
        String email = auth.getName();

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Trainer by id:"
                        + trainerId));

        String trainerEmail = trainer.getUserCredentials().getEmail();
        boolean canModify = email.equals(trainerEmail);

        log.debug("User email: {}, Trainer email: {}, Can modify: {}",
                email, trainerEmail, canModify);
        return canModify;
    }

    @Override
    public void deleteTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Trainer by id:" + id));
        trainerRepository.deleteById(id);
        log.debug("Trainer was deleted with ID: {}", id);
    }

    @Override
    public TrainerResponseDto updateTrainerByAuth(Authentication authentication,
                                                  TrainerUpdateRequestDto requestDto) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        String email = authentication.getName();
        log.debug("Updating trainer for email: {}", email);

        Trainer existingTrainer = trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found for email: "
                        + email));

        Optional.ofNullable(requestDto.firstName()).ifPresent(existingTrainer::setFirstName);
        Optional.ofNullable(requestDto.lastName()).ifPresent(existingTrainer::setLastName);
        Optional.ofNullable(requestDto.gender()).ifPresent(existingTrainer::setGender);
        Optional.ofNullable(requestDto.dateOfBirth()).ifPresent(existingTrainer::setDateOfBirth);
        Optional.ofNullable(requestDto.profileImageUrl())
                .ifPresent(existingTrainer::setProfileImageUrl);
        Optional.ofNullable(requestDto.onlineTraining())
                .ifPresent(existingTrainer::setOnlineTraining);
        Optional.ofNullable(requestDto.description()).ifPresent(existingTrainer::setDescription);
        Optional.ofNullable(requestDto.socialMediaLinks())
                .ifPresent(existingTrainer::setSocialMediaLinks);

        updateEntities(existingTrainer::setSports, requestDto.sportIds(),
                sportRepository::findById, "Sport with id ");
        updateEntities(existingTrainer::setGyms, requestDto.gymIds(),
                gymRepository::findById, "Gym with id ");
        updateLocation(existingTrainer, requestDto.location());

        Trainer updatedTrainer = trainerRepository.save(existingTrainer);
        TrainerResponseDto dto = trainerMapper.toDto(updatedTrainer);
        return dto;
    }

    private void updateLocation(Trainer trainer, TrainerAddressDto locationDto) {
        if (locationDto != null) {
            Address existingAddress = trainer.getLocation();
            String street = locationDto.street() != null ? locationDto.street() : "";
            String house = locationDto.house() != null ? locationDto.house() : "";
            Optional<Address> foundAddress = addressRepository
                    .findByCountryAndCityAndStreetAndHouse(
                            locationDto.country(),
                            locationDto.city(),
                            street, house
                    );

            Address address = foundAddress.orElseGet(() -> {
                Address newAddress = (existingAddress != null) ? existingAddress : new Address();
                newAddress.setCountry(locationDto.country());
                newAddress.setCity(locationDto.city());
                newAddress.setCityDistrict(locationDto.cityDistrict());
                newAddress.setStreet(street);
                newAddress.setHouse(house);
                return newAddress;
            });

            Optional.ofNullable(locationDto.cityDistrict()).ifPresent(address::setCityDistrict);
            trainer.setLocation(address);
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
            setter.accept(entities);
        }
    }
}
