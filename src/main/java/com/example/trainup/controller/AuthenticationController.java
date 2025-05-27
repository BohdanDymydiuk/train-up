package com.example.trainup.controller;

import com.example.trainup.dto.users.UserLoginRequestDto;
import com.example.trainup.dto.users.UserLoginResponseDto;
import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.security.AuthenticationService;
import com.example.trainup.service.AthleteService;
import com.example.trainup.service.GymOwnerService;
import com.example.trainup.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "Authentication and Registration",
        description = "Endpoints for user login and new user registration across different roles."
)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AthleteService athleteService;
    private final TrainerService trainerService;
    private final GymOwnerService gymOwnerService;

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user with provided credentials and issues JWT access and"
                    + " refresh tokens upon successful login."
    )
    public UserLoginResponseDto loginAthlete(@Valid @RequestBody UserLoginRequestDto requestDto) {
        log.info("Attempting user login for username login: {}", requestDto.email());
        UserLoginResponseDto loginResponseDto = authenticationService.authenticate(requestDto);

        log.info("User {} successfully logged in.", requestDto.email());
        return loginResponseDto;
    }

    @PostMapping("/register/athlete")
    @Operation(
            summary = "Athlete Registration",
            description = "Registers a new athlete user with the provided details."
    )
    @ResponseStatus(HttpStatus.CREATED)
    public AthleteResponseDto registerAthlete(
            @Valid @RequestBody AthleteRegistrationRequestDto requestDto) {
        log.info("Attempting athlete registration with email: {}", requestDto.email());
        AthleteResponseDto registeredAthlete = athleteService.register(requestDto);

        log.info("Athlete with email: {} successfully registered. ID: {}",
                requestDto.email(), registeredAthlete.id());
        return registeredAthlete;
    }

    @PostMapping("/register/trainer")
    @Operation(
            summary = "Trainer Registration",
            description = "Registers a new trainer user with the provided details."
    )
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerResponseDto registerTrainer(
            @Valid @RequestBody TrainerRegistrationRequestDto requestDto) {
        log.info("Attempting trainer registration with email: {}", requestDto.email());
        TrainerResponseDto registeredTrainer = trainerService.register(requestDto);

        log.info("Trainer with email: {} successfully registered. ID: {}",
                requestDto.email(), registeredTrainer.id());
        return registeredTrainer;
    }

    @PostMapping("/register/gym_owner")
    @Operation(
            summary = "Gym Owner Registration",
            description = "Registers a new gym owner user with the provided details."
    )
    @ResponseStatus(HttpStatus.CREATED)
    public GymOwnerResponseDto registerGymOwner(
            @Valid @RequestBody GymOwnerRegistrationRequestDto requestDto) {
        log.info("Attempting gym owner registration with email: {}",
                requestDto.email());
        GymOwnerResponseDto registeredGymOwner = gymOwnerService.register(requestDto);

        log.info("Gym owner with email: {} successfully registered. ID: {}",
                requestDto.email(), registeredGymOwner.id());
        return registeredGymOwner;
    }
}
