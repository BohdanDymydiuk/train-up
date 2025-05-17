package com.example.trainup.controller;

import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.TrainerService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TrainerController {
    private final TrainerService trainerService;
    private final CurrentUserService currentUserService;

    @GetMapping
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
        List<TrainerResponseDto> trainers = trainerService.getAllTrainers(filter, pageable);
        return trainers;
    }

    @GetMapping("/my")
    public TrainerResponseDto getCurrentTrainer(Authentication authentication) {
        TrainerResponseDto responseDto = trainerService.getTrainerByAuth(authentication);
        return responseDto;
    }

    @GetMapping("/{id}")
    public TrainerResponseDto getTrainerById(@PathVariable @Positive Long id) {
        TrainerResponseDto responseDto = trainerService.getTrainerById(id);
        return responseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @trainerServiceImpl.canUserModifyTrainer(#authentication, #id)")
    public ResponseEntity<Void> deleteTrainerById(@PathVariable @Positive Long id,
                                                  Authentication authentication) {
        trainerService.deleteTrainerById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@trainerServiceImpl.canUserModifyTrainer(#authentication, #id)")
    public TrainerResponseDto updateTrainerById(@RequestBody TrainerUpdateRequestDto requestDto,
                                                @PathVariable @Positive Long id,
                                            Authentication authentication) {
        TrainerResponseDto responseDto = trainerService
                .updateTrainerByAuth(authentication, requestDto);
        return responseDto;
    }
}
