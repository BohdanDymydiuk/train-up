package com.example.trainup.service;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import com.example.trainup.mapper.AthleteMapper;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.SportRepository;
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
@Transactional
@Log4j2
public class AthleteServiceImpl implements AthleteService {
    private final AthleteMapper athleteMapper;
    private final PasswordEncoder passwordEncoder;
    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;
    private final UserCredentialService userCredentialService;

    @Override
    public AthleteResponseDto register(AthleteRegistrationRequestDto requestDto) {
        Athlete athlete = athleteMapper.toModel(requestDto, passwordEncoder, sportRepository);
        athleteRepository.save(athlete);
        userCredentialService.assignRoleBasedOnUserType(athlete.getUserCredentials());
        return athleteMapper.toDto(athlete);
    }

    @Override
    public AthleteResponseDto getAthleteById(Long id) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Athlete by id: "
                        + id));
        AthleteResponseDto dto = athleteMapper.toDto(athlete);
        return dto;
    }

    @Override
    public boolean canUserModifyAthlete(Authentication auth, Long id) {
        if (auth == null || id == null) {
            return false;
        }

        String email = auth.getName();
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Athlete by id: "
                        + id));
        String athleteEmail = athlete.getUserCredentials().getEmail();
        boolean canModify = email.equals(athleteEmail);

        log.debug("User email: {}, Athlete email: {}, Can modify: {}",
                email, athleteEmail, canModify);
        return canModify;
    }

    @Override
    public List<AthleteResponseDto> getAllAthlete(AthleteFilterRequestDto filter,
                                                  Pageable pageable) {
        Page<Athlete> athletePage = athleteRepository.findAthleteByCriteria(
                filter.firstName(),
                filter.lastName(),
                filter.maleOrFemale(),
                filter.dateOfBirth(),
                filter.sportIds(),
                filter.emailPermission(),
                filter.phonePermission(),
                pageable
        );

        return athletePage.stream()
                .map(athleteMapper::toDto)
                .toList();
    }

    @Override
    public void deleteAthleteById(Long id) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find Athlete by id:"
                        + id));
        athleteRepository.deleteById(id);
        log.debug("Athlete was deleted with ID: {}", id);
    }

    @Override
    public AthleteResponseDto updateAthleteByAuth(Authentication authentication,
                                                  AthleteUpdateRequestDto requestDto) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        String email = authentication.getName();
        log.debug("Updating athlete for email: {}", email);

        Athlete existingAthlete = athleteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Athlete not found for email: "
                                + email));

        Optional.ofNullable(requestDto.firstName())
                .ifPresent(existingAthlete::setFirstName);
        Optional.ofNullable(requestDto.lastName())
                .ifPresent(existingAthlete::setLastName);
        Optional.ofNullable(requestDto.maleOrFemale())
                .ifPresent(existingAthlete::setMaleOrFemale);
        Optional.ofNullable(requestDto.dateOfBirth())
                .ifPresent(existingAthlete::setDateOfBirth);
        Optional.ofNullable(requestDto.profileImageUrl())
                .ifPresent(existingAthlete::setProfileImageUrl);
        Optional.ofNullable(requestDto.phoneNumbers())
                .ifPresent(existingAthlete::setPhoneNumbers);
        Optional.ofNullable(requestDto.emailPermission())
                .ifPresent(existingAthlete::setEmailPermission);
        Optional.ofNullable(requestDto.phonePermission())
                .ifPresent(existingAthlete::setPhonePermission);

        updateEntities(existingAthlete::setSports, requestDto.sportIds(),
                sportRepository::findById, "Sport with id ");

        Athlete updatedAthlete = athleteRepository.save(existingAthlete);
        AthleteResponseDto dto = athleteMapper.toDto(updatedAthlete);
        return dto;
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
