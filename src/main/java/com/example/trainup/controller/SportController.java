package com.example.trainup.controller;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.service.SportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sport")
@Validated
@RequiredArgsConstructor
@Log4j2
public class SportController {
    private final SportService sportService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SportDto> getAllSports(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        SportDto sportFilterDto = new SportDto(id, name);
        List<SportDto> responseDtos = sportService.getAllSports(sportFilterDto, pageable);
        return responseDtos;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SportDto createSport(@RequestBody @Valid SportRequestDto requestDto) {
        SportDto sportDto = sportService.createSport(requestDto);
        return sportDto;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SportDto updateSport(@PathVariable @Positive Long id,
                         @RequestBody SportRequestDto requestDto) {
        SportDto sportDto = sportService.updateSport(id, requestDto);
        return sportDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSport(@PathVariable @Positive Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}
