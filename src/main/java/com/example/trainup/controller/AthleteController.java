package com.example.trainup.controller;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.service.users.AthleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
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
@RequestMapping("/athlete")
@Validated
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "Athlete Management API",
        description = "Endpoints for managing athlete profiles and data."
)
public class AthleteController {
    private final AthleteService athleteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @athleteServiceImpl.canUserModifyAthlete(#authentication, #id)")
    @Operation(
            summary = "Retrieve Athlete Profile by ID",
            description = "Allows an ADMIN or the athlete themselves to retrieve detailed profile "
                    + "information for a specific athlete."
    )
    public AthleteResponseDto getAthleteById(@PathVariable @Positive Long id,
                                      Authentication authentication) {
        log.info("Attempting to retrieve athlete with ID: {} by user: {}",
                id, authentication.getName());
        AthleteResponseDto responseDto = athleteService.getAthleteById(id);

        log.info("Successfully retrieved athlete with ID: {}", id);
        return responseDto;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Retrieve Athletes by Criteria",
            description = "Allows ADMIN users to search and retrieve a paginated list of athletes "
                    + "based on various filtering criteria."
    )
    public List<AthleteResponseDto> getAllAthlete(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) Set<Long> sportIds,
            @RequestParam(required = false) Boolean emailPermission,
            @RequestParam(required = false) Boolean phonePermission,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("Attempting to retrieve all athletes with filter: "
                        + "firstName={}, lastName={}, gender={}, dateOfBirth={}, sportIds={},"
                        + " emailPermission={}, phonePermission={}",
                firstName, lastName, gender, dateOfBirth, sportIds,
                emailPermission, phonePermission);
        AthleteFilterRequestDto filter = new AthleteFilterRequestDto(firstName, lastName,
                gender, dateOfBirth, sportIds, emailPermission, phonePermission);
        List<AthleteResponseDto> athleteResponseDtos = athleteService
                .getAllAthlete(filter, pageable);

        log.info("Successfully retrieved {} athletes with specified criteria.",
                athleteResponseDtos.size());
        return athleteResponseDtos;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@athleteService.canUserModifyAthlete(#authentication, #id)")
    @Operation(
            summary = "Update Athlete Profile",
            description = "Allows an athlete to update their own profile information. "
                    + "Only specific fields can be updated."
    )
    public AthleteResponseDto updateAthleteById(@RequestBody AthleteUpdateRequestDto requestDto,
                                         Authentication authentication,
                                         @PathVariable @Positive Long id) {
        log.info("Attempting to update athlete with ID: {} by user: {}",
                id, authentication.getName());
        AthleteResponseDto athleteResponseDto = athleteService
                .updateAthleteByAuth(authentication, requestDto);

        log.info("Successfully updated athlete with ID: {}", id);
        return athleteResponseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @athleteServiceImpl.canUserModifyAthlete(#authentication, #id)")
    @Operation(
            summary = "Delete Athlete User",
            description = "Allows an ADMIN or the athlete themselves to delete "
                    + "an athlete's user account."
    )
    public ResponseEntity<Void> deleteAthleteById(@PathVariable @Positive Long id,
                                           Authentication authentication) {
        log.info("Attempting to delete athlete with ID: {} by user: {}",
                id, authentication.getName());
        athleteService.deleteAthleteById(id);

        log.info("Successfully deleted athlete with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
