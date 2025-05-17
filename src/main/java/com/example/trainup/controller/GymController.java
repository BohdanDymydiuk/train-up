package com.example.trainup.controller;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.GymService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym")
@Validated
@RequiredArgsConstructor
@Slf4j
public class GymController {
    private final GymService gymService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize("hasRole('GYM_OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public GymResponseDto createGym(@RequestBody @Valid GymRegistrationRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserCredentials)) {
            throw new IllegalStateException("User is not authenticated "
                    + "or principal is not UserCredentials");
        }
        GymOwner gymOwner = currentUserService.getCurrentUserByType(GymOwner.class);

        GymResponseDto savedGym = gymService.save(gymOwner, requestDto);
        return savedGym;
    }

    @GetMapping
    public List<GymResponseDto> getAllGyms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String locationCountry,
            @RequestParam(required = false) String locationCity,
            @RequestParam(required = false) String locationCityDistrict,
            @RequestParam(required = false) String locationStreet,
            @RequestParam(required = false) String locationHouse,
            @RequestParam(required = false) Set<Long> sportIds,
            @RequestParam(required = false) Set<Long> trainerIds,
            @RequestParam(required = false) Float overallRating,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        GymFilterRequestDto filter = new GymFilterRequestDto(
                name, locationCountry, locationCity, locationCityDistrict,
                locationStreet, locationHouse, sportIds, trainerIds, overallRating
        );
        List<GymResponseDto> gyms = gymService.getAllGyms(filter, pageable);
        return gyms;
    }

    @GetMapping("/my")
    public Page<GymResponseDto> getGymsByGymsOwnerId(
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        log.debug("Fetching gyms for current gym owner with pageable: {}", pageable);

        Page<GymResponseDto> gymOwnerGyms = gymService.getGymsByGymOwner(authentication, pageable);
        return gymOwnerGyms;
    }

    @GetMapping("/{id}")
    public GymResponseDto getGymById(@PathVariable @Positive Long id) {
        GymResponseDto gymResponseDto = gymService.getGymById(id);
        return gymResponseDto;

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @gymServiceImpl.canUserModifyGym(#authentication.name, #id)")
    public ResponseEntity<Void> deleteGymById(@PathVariable @Positive Long id,
                                              Authentication authentication) {
        log.debug("Deleting gym with ID: {}", id);
        gymService.deleteGymById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@gymServiceImpl.canUserModifyGym(#authentication.name, #id)")
    public GymResponseDto updateGym(@PathVariable @Positive Long id,
                                    @RequestBody GymUpdateRequestDto requestDto,
                                    Authentication authentication) {
        GymResponseDto updatedGym = gymService.updateGym(id, requestDto);
        return updatedGym;
    }
}
