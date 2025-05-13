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
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym")
@Validated
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;
    private final CurrentUserService currentUserService;

    @PostMapping
    //    @PreAuthorize("hasRole('GYM_OWNER')")
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
    public List<GymResponseDto> getAllGyms(@RequestBody GymFilterRequestDto filterRequestDto,
                                           @ParameterObject @PageableDefault Pageable pageable) {
        List<GymResponseDto> gyms = gymService.getAllGyms(filterRequestDto, pageable);

        return gyms;
    }
}
