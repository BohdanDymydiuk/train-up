package com.example.trainup.controller;

import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import com.example.trainup.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
@Validated
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "Trainer Management",
        description = "Endpoints for managing trainer profiles and data."
)
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping
    @Operation(
            summary = "Retrieve Trainers by Criteria",
            description = "Retrieves a paginated list of trainers based on various filtering "
                    + "criteria."
    )
    public List<TrainerResponseDto> getAllTrainers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Character maleOrFemale,
            @RequestParam(required = false) Set<Long> sportIds,
            @RequestParam(required = false) Set<Long> gymIds,
            @RequestParam(required = false) String locationCountry,
            @RequestParam(required = false) String locationCity,
            @RequestParam(required = false) String locationCityDistrict,
            @RequestParam(required = false) String locationStreet,
            @RequestParam(required = false) String locationHouse,
            @RequestParam(required = false) Boolean onlineTraining,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        TrainerFilterRequestDto filter = new TrainerFilterRequestDto(firstName, lastName,
                maleOrFemale, sportIds, gymIds, locationCountry, locationCity,
                locationCityDistrict, locationStreet, locationHouse, onlineTraining);
        log.info("Attempting to fetch trainers with filter: {} and pageable: {}",
                filter, pageable);

        List<TrainerResponseDto> trainers = trainerService.getAllTrainers(filter, pageable);

        log.info("Successfully retrieved {} trainers with specified criteria.", trainers.size());
        return trainers;
    }

    @GetMapping("/my")
    @Operation(
            summary = "Get Current Trainer's Profile",
            description = "Retrieves the detailed profile information for the currently "
                    + "authenticated Trainer user."
    )
    public TrainerResponseDto getCurrentTrainer(Authentication authentication) {
        log.info("Attempting to retrieve profile for current trainer: {}",
                authentication.getName());
        TrainerResponseDto responseDto = trainerService.getTrainerByAuth(authentication);

        log.info("Successfully retrieved profile for current trainer: {}",
                authentication.getName());
        return responseDto;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve Trainer by ID",
            description = "Retrieves detailed profile information for a specific trainer "
                    + "by their ID."
    )
    public TrainerResponseDto getTrainerById(@PathVariable @Positive Long id) {
        log.info("Attempting to retrieve trainer with ID: {}", id);
        TrainerResponseDto responseDto = trainerService.getTrainerById(id);

        log.info("Successfully retrieved trainer with ID: {}", id);
        return responseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @trainerServiceImpl.canUserModifyTrainer(#authentication, #id)")
    @Operation(
            summary = "Delete Trainer",
            description = "Allows an ADMIN or the Trainer themselves to delete a trainer's user "
                    + "account."
    )
    public ResponseEntity<Void> deleteTrainerById(@PathVariable @Positive Long id,
                                                  Authentication authentication) {
        log.info("Attempting to delete trainer with ID: {} by user: {}",
                id, authentication.getName());
        trainerService.deleteTrainerById(id);

        log.info("Trainer with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@trainerServiceImpl.canUserModifyTrainer(#authentication, #id)")
    @Operation(
            summary = "Update Trainer Profile",
            description = "Allows the Trainer themselves to update their own profile information. "
                    + "Only specific fields can be updated."
    )
    public TrainerResponseDto updateTrainerById(@RequestBody TrainerUpdateRequestDto requestDto,
                                                @PathVariable @Positive Long id,
                                            Authentication authentication) {
        log.info("Attempting to update trainer with ID: {} by user '{}' with request: {}",
                id, authentication.getName(), requestDto);
        TrainerResponseDto responseDto = trainerService
                .updateTrainerByAuth(authentication, requestDto);

        log.info("Trainer with ID: {} successfully updated.", id);
        return responseDto;
    }
}
