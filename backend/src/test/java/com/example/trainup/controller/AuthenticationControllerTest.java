package com.example.trainup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.users.UserLoginRequestDto;
import com.example.trainup.dto.users.UserLoginResponseDto;
import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.UserCredentials.UserType;
import com.example.trainup.security.AuthenticationService;
import com.example.trainup.service.users.AthleteService;
import com.example.trainup.service.users.GymOwnerService;
import com.example.trainup.service.users.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AthleteService athleteService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private GymOwnerService gymOwnerService;

    private UserLoginRequestDto loginRequestDto;
    private AthleteRegistrationRequestDto athleteRegistrationRequestDto;
    private TrainerRegistrationRequestDto trainerRegistrationRequestDto;
    private GymOwnerRegistrationRequestDto gymOwnerRegistrationRequestDto;

    @BeforeEach
    void setup() {
        loginRequestDto = new UserLoginRequestDto("test@example.com", "Password#1");

        athleteRegistrationRequestDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "test@example.com",
                "Password#1",
                "Password#1",
                Set.of("+38(050)-123-4567"),
                Set.of(1L),
                true,
                true
        );

        trainerRegistrationRequestDto = new TrainerRegistrationRequestDto(
                "Jane",
                "Smith",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 5),
                "http://example.com/trainer.jpg",
                "trainer@example.com",
                "Password#1",
                "Password#1",
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                Set.of(1L),
                new TrainerAddressDto("Ukraine", "Kyiv", null, null, null),
                true,
                List.of("Certificate1.pdf"),
                "Experienced trainer",
                "http://linkedin.com/jane"
        );

        gymOwnerRegistrationRequestDto = new GymOwnerRegistrationRequestDto(
                "Bob",
                "Johnson",
                Gender.MALE,
                LocalDate.of(1980, 10, 10),
                "http://example.com/owner.jpg",
                "owner@example.com",
                "Password#1",
                "Password#1",
                Set.of("+38(050)-555-5555")
        );
    }

    @Test
    void login_Success() throws Exception {
        // Given
        UserLoginResponseDto responseDto = new UserLoginResponseDto("jwt_access_token");

        // When
        when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenReturn(responseDto);

        // Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_access_token"));
    }

    @Test
    void login_InvalidEmail_400() throws Exception {
        // Given
        UserLoginRequestDto invalidDto = new UserLoginRequestDto("invalid-email", "Password#1");

        // Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("email: Email is not valid"));
    }

    @Test
    void login_InvalidPassword_400() throws Exception {
        // Given
        UserLoginRequestDto invalidDto = new UserLoginRequestDto("test@example.com", "password");

        // Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("password: must match \"^(?=.*[a-z])(?=."
                        + "*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$\""));
    }

    @Test
    void registerAthlete_Success() throws Exception {
        // Given
        AthleteResponseDto responseDto = new AthleteResponseDto(
                1L,
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "test@example.com",
                UserType.ATHLETE.name(),
                Set.of("+38(050)-123-4567"),
                Set.of(1L),
                true,
                true
        );

        // When
        when(athleteService.register(any(AthleteRegistrationRequestDto.class)))
                .thenReturn(responseDto);

        // Then
        mockMvc.perform(post("/auth/register/athlete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(athleteRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(050)-123-4567"))
                .andExpect(jsonPath("$.sportIds[0]").value(1))
                .andExpect(jsonPath("$.emailPermission").value(true))
                .andExpect(jsonPath("$.phonePermission").value(true));
    }

    @Test
    void registerAthlete_PasswordMismatch_400() throws Exception {
        // Given
        AthleteRegistrationRequestDto invalidDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "test@example.com",
                "Password#1",
                "Password#2",
                Set.of("+38(050)-123-4567"),
                Set.of(1L),
                true,
                true
        );

        // Then
        mockMvc.perform(post("/auth/register/athlete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("The password fields must match"));
    }

    @Test
    void registerAthlete_InvalidEmail_400() throws Exception {
        // Given
        AthleteRegistrationRequestDto invalidDto = new AthleteRegistrationRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "invalid-email",
                "Password#1",
                "Password#1",
                Set.of("+38(050)-123-4567"),
                Set.of(1L),
                true,
                true
        );

        // Then
        mockMvc.perform(post("/auth/register/athlete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("email: Email is not valid"));
    }

    @Test
    void registerTrainer_Success() throws Exception {
        // Given
        TrainerResponseDto responseDto = new TrainerResponseDto(
                1L,
                "Jane",
                "Smith",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 5),
                "http://example.com/trainer.jpg",
                "trainer@example.com",
                UserType.TRAINER.name(),
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                Set.of(1L),
                new TrainerAddressDto("Ukraine", "Kyiv", null, null, null),
                "Experienced trainer",
                "http://linkedin.com/jane",
                0F,
                0
        );

        // When
        when(trainerService.register(any(TrainerRegistrationRequestDto.class)))
                .thenReturn(responseDto);

        // Then
        mockMvc.perform(post("/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.email").value("trainer@example.com"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(050)-987-6543"))
                .andExpect(jsonPath("$.sportIds[0]").value(1))
                .andExpect(jsonPath("$.gymIds[0]").value(1));
    }

    @Test
    void registerTrainer_PasswordMismatch_400() throws Exception {
        // Given
        TrainerRegistrationRequestDto invalidDto = new TrainerRegistrationRequestDto(
                "Jane",
                "Smith",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 5),
                "http://example.com/trainer.jpg",
                "trainer@example.com",
                "Password#1",
                "Password#2",
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                Set.of(1L),
                new TrainerAddressDto("Ukraine", "Kyiv", null, null, null),
                true,
                List.of("Certificate1.pdf"),
                "Experienced trainer",
                "http://linkedin.com/jane"
        );

        // Then
        mockMvc.perform(post("/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("The password fields must match"));
    }

    @Test
    void registerTrainer_EmptySportIds_400() throws Exception {
        // Given
        TrainerRegistrationRequestDto invalidDto = new TrainerRegistrationRequestDto(
                "Jane",
                "Smith",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 5),
                "http://example.com/trainer.jpg",
                "trainer@example.com",
                "Password#1",
                "Password#1",
                Set.of("+38(050)-987-6543"),
                Set.of(),
                Set.of(1L),
                new TrainerAddressDto("Ukraine", "Kyiv", null, null, null),
                true,
                List.of("Certificate1.pdf"),
                "Experienced trainer",
                "http://linkedin.com/jane"
        );

        // Then
        mockMvc.perform(post("/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("sportIds: Sport Ids cannot be empty"));
    }

    @Test
    void registerGymOwner_Success() throws Exception {
        // Given
        GymOwnerResponseDto responseDto = new GymOwnerResponseDto(
                1L,
                "Bob",
                "Johnson",
                Gender.MALE,
                LocalDate.of(1980, 10, 10),
                "http://example.com/owner.jpg",
                "owner@example.com",
                UserType.GYM_OWNER.name(),
                Set.of("+38(050)-555-5555"),
                null
        );

        // When
        when(gymOwnerService.register(any(GymOwnerRegistrationRequestDto.class)))
                .thenReturn(responseDto);

        // Then
        mockMvc.perform(post("/auth/register/gym_owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymOwnerRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.email").value("owner@example.com"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(050)-555-5555"));
    }

    @Test
    void registerGymOwner_PasswordMismatch_400() throws Exception {
        // Given
        GymOwnerRegistrationRequestDto invalidDto = new GymOwnerRegistrationRequestDto(
                "Bob",
                "Johnson",
                Gender.MALE,
                LocalDate.of(1980, 10, 10),
                "http://example.com/owner.jpg",
                "owner@example.com",
                "Password#1",
                "Password#2",
                Set.of("+38(050)-555-5555")
        );

        // Then
        mockMvc.perform(post("/auth/register/gym_owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("The password fields must match"));
    }

    @Test
    void registerGymOwner_EmptyPhoneNumbers_400() throws Exception {
        // Given
        GymOwnerRegistrationRequestDto invalidDto = new GymOwnerRegistrationRequestDto(
                "Bob",
                "Johnson",
                Gender.MALE,
                LocalDate.of(1980, 10, 10),
                "http://example.com/owner.jpg",
                "owner@example.com",
                "Password#1",
                "Password#1",
                Set.of()
        );

        // Then
        mockMvc.perform(post("/auth/register/gym_owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("phoneNumbers: Enter your phone number"));
    }
}
