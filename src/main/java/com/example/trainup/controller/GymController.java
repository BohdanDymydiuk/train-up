package com.example.trainup.controller;

import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.GymService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Tag(
        name = "Gym Management",
        description = "Endpoints for managing gym information, including creation, retrieval, "
                + "update, and deletion."
)
public class GymController {
    private final GymService gymService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize("hasRole('GYM_OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create New Gym",
            description = "Allows a Gym Owner to register a new gym. The gym will be associated "
                    + "with the currently authenticated Gym Owner."
    )
    public GymResponseDto createGym(@RequestBody @Valid GymRegistrationRequestDto requestDto) {
        log.info("Attempting to create a new gym with request: {}", requestDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserCredentials)) {
            throw new IllegalStateException("User is not authenticated "
                    + "or principal is not UserCredentials");
        }
        GymOwner gymOwner = currentUserService.getCurrentUserByType(GymOwner.class);
        GymResponseDto savedGym = gymService.save(gymOwner, requestDto);

        log.info("Gym successfully created with ID: {} by GymOwner: {}",
                savedGym.id(), gymOwner.getId());
        return savedGym;
    }

    @GetMapping
    @Operation(
            summary = "Retrieve Gyms by Criteria",
            description = "Retrieves a paginated list of gyms based on various filtering criteria. "
                    + "Accessible to all authenticated users."
    )
    public List<GymResponseDto> getAllGyms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String locationCountry,
            @RequestParam(required = false) String locationCity,
            @RequestParam(required = false) String locationCityDistrict,
            @RequestParam(required = false) String locationStreet,
            @RequestParam(required = false) String locationHouse,
            @RequestParam(required = false) Set<Long> sportIds,
            @RequestParam(required = false) Set<Long> trainerIds,
            @RequestParam(required = false) @Positive Float overallRating,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        GymFilterRequestDto filter = new GymFilterRequestDto(
                name, locationCountry, locationCity, locationCityDistrict,
                locationStreet, locationHouse, sportIds, trainerIds, overallRating
        );
        log.info("Attempting to fetch gyms with filter: {} and pageable: {}", filter, pageable);

        List<GymResponseDto> gyms = gymService.getAllGyms(filter, pageable);

        log.info("Successfully retrieved {} gyms with specified criteria.", gyms.size());
        return gyms;
    }

    @GetMapping("/my")
    @Operation(
            summary = "Get Gyms owned by Current Gym Owner",
            description = "Retrieves a paginated list of gyms associated with the currently "
                    + "authenticated Gym Owner."
    )
    public Page<GymResponseDto> getGymsByGymsOwnerId(
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        log.info("Attempting to fetch gyms for current gym owner: {} with pageable: {}",
                authentication.getName(), pageable);

        Page<GymResponseDto> gymOwnerGyms = gymService.getGymsByGymOwner(authentication, pageable);

        log.info("Successfully retrieved {} gyms for current gym owner {}.",
                gymOwnerGyms.getTotalElements(), authentication.getName());
        return gymOwnerGyms;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve Gym by ID",
            description = "Retrieves detailed information for a specific gym by its ID. Accessible "
                    + "to all authenticated users."
    )
    public GymResponseDto getGymById(@PathVariable @Positive Long id) {
        log.info("Attempting to retrieve gym with ID: {}", id);
        GymResponseDto gymResponseDto = gymService.getGymById(id);

        log.info("Successfully retrieved gym with ID: {}", id);
        return gymResponseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @gymServiceImpl.canUserModifyGym(#authentication, #id)")
    @Operation(
            summary = "Delete Gym by ID",
            description = "Allows an ADMIN or the Gym Owner to delete a specific gym. "
                    + "The Gym Owner must own the gym."
    )
    public ResponseEntity<Void> deleteGymById(@PathVariable @Positive Long id,
                                              Authentication authentication) {
        log.info("Attempting to delete gym with ID: {} by user: {}",
                id, authentication.getName());
        gymService.deleteGymById(id);

        log.info("Gym with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@gymServiceImpl.canUserModifyGym(#authentication, #id)")
    @Operation(
            summary = "Update Gym Information",
            description = "Allows the Gym Owner to update specific details of a gym they own."
    )
    public GymResponseDto updateGym(@PathVariable @Positive Long id,
                                    @RequestBody GymUpdateRequestDto requestDto,
                                    Authentication authentication) {
        log.info("Attempting to update gym with ID: {} by user '{}' with request: {}",
                id, authentication.getName(), requestDto);
        GymResponseDto updatedGym = gymService.updateGym(id, requestDto);

        log.info("Gym with ID: {} successfully updated.", id);
        return updatedGym;
    }
}
