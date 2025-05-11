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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AthleteService athleteService;
    private final TrainerService trainerService;
    private final GymOwnerService gymOwnerService;

    @GetMapping("/login")
    public UserLoginResponseDto loginAthlete(@Valid @RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto loginResponseDto = authenticationService.authenticate(requestDto);
        return loginResponseDto;
    }

    @PostMapping("/register/athlete")
    public AthleteResponseDto registerAthlete(
            @Valid @RequestBody AthleteRegistrationRequestDto requestDto) {
        AthleteResponseDto registeredAthlete = athleteService.register(requestDto);
        return registeredAthlete;
    }

    @PostMapping("/register/trainer")
    public TrainerResponseDto registerTrainer(
            @Valid @RequestBody TrainerRegistrationRequestDto requestDto) {
        TrainerResponseDto registeredTrainer = trainerService.register(requestDto);
        return registeredTrainer;
    }

    @PostMapping("/register/gym_owner")
    public GymOwnerResponseDto registerGymOwner(
            @Valid @RequestBody GymOwnerRegistrationRequestDto requestDto) {
        GymOwnerResponseDto registeredGymOwner = gymOwnerService.register(requestDto);
        return registeredGymOwner;
    }
}
