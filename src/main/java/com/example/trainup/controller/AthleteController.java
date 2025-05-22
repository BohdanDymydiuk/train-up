package com.example.trainup.controller;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import com.example.trainup.service.AthleteService;
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
public class AthleteController {
    private final AthleteService athleteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @athleteServiceImpl.canUserModifyAthlete(#authentication, #id)")
    public AthleteResponseDto getAthleteById(@PathVariable @Positive Long id,
                                      Authentication authentication) {
        AthleteResponseDto responseDto = athleteService.getAthleteById(id);
        return responseDto;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AthleteResponseDto> getAllAthlete(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Character maleOrFemale,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) Set<Long> sportIds,
            @RequestParam(required = false) Boolean emailPermission,
            @RequestParam(required = false) Boolean phonePermission,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        AthleteFilterRequestDto filter = new AthleteFilterRequestDto(firstName, lastName,
                maleOrFemale, dateOfBirth, sportIds, emailPermission, phonePermission);
        List<AthleteResponseDto> athleteResponseDtos = athleteService
                .getAllAthlete(filter, pageable);
        return athleteResponseDtos;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@athleteServiceImpl.canUserModifyAthlete(#authentication, #id)")
    public AthleteResponseDto updateAthleteById(@RequestBody AthleteUpdateRequestDto requestDto,
                                         Authentication authentication,
                                         @PathVariable @Positive Long id) {
        AthleteResponseDto athleteResponseDto = athleteService
                .updateAthleteByAuth(authentication, requestDto);
        return athleteResponseDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') "
            + "or @athleteServiceImpl.canUserModifyAthlete(#authentication, #id)")
    public ResponseEntity<Void> deleteAthleteById(@PathVariable @Positive Long id,
                                           Authentication authentication) {
        athleteService.deleteAthleteById(id);
        return ResponseEntity.noContent().build();
    }
}
