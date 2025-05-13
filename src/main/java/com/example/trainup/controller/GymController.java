package com.example.trainup.controller;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.GymService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym")
@Validated
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize("hasRole('GYM_OWNER')")
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
}
