package com.example.trainup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.users.athlete.AthleteFilterRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.model.user.UserCredentials.UserType;
import com.example.trainup.service.users.AthleteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AthleteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "athleteService")
    private AthleteService athleteService;

    private Athlete athlete;
    private Sport sport;

    @BeforeEach
    void setUp() {
        sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Football");

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setId(1L);
        userCredentials.setEmail("john.doe@example.com");
        userCredentials.setUserType(UserType.ATHLETE);

        athlete = new Athlete();
        athlete.setId(1L);
        athlete.setFirstName("John");
        athlete.setLastName("Doe");
        athlete.setGender(Gender.MALE);
        athlete.setDateOfBirth(LocalDate.of(1990, 1, 1));
        athlete.setProfileImageUrl("http://example.com/image.jpg");
        athlete.setPhoneNumbers(Set.of("+38(050)-123-4567"));
        athlete.setSports(Set.of(sport));
        athlete.setEmailPermission(true);
        athlete.setPhonePermission(true);
        athlete.setUserCredentials(userCredentials);
    }

    @Test
    void getAthleteById_Self_Success() throws Exception {
        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(true);
        when(athleteService.getAthleteById(athlete.getId())).thenReturn(
                new AthleteResponseDto(
                        athlete.getId(),
                        athlete.getFirstName(),
                        athlete.getLastName(),
                        athlete.getGender(),
                        athlete.getDateOfBirth(),
                        athlete.getProfileImageUrl(),
                        athlete.getUserCredentials().getEmail(),
                        athlete.getUserCredentials().getUserType().name(),
                        athlete.getPhoneNumbers(),
                        athlete.getSports().stream().map(Sport::getId).collect(Collectors.toSet()),
                        athlete.getEmailPermission(),
                        athlete.getPhonePermission()
                )
        );

        // Then
        mockMvc.perform(get("/athlete/{id}", athlete.getId())
                        .with(user("john.doe@example.com").roles("ATHLETE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(athlete.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(050)-123-4567"));
    }

    @Test
    void getAthleteById_Unauthorized_403() throws Exception {
        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(get("/athlete/{id}", athlete.getId())
                        .with(user("other.user@example.com").roles("ATHLETE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllAthlete_Admin_Success() throws Exception {
        // Given
        AthleteResponseDto responseDto = new AthleteResponseDto(
                athlete.getId(),
                athlete.getFirstName(),
                athlete.getLastName(),
                athlete.getGender(),
                athlete.getDateOfBirth(),
                athlete.getProfileImageUrl(),
                athlete.getUserCredentials().getEmail(),
                athlete.getUserCredentials().getUserType().name(),
                athlete.getPhoneNumbers(),
                athlete.getSports().stream().map(Sport::getId).collect(Collectors.toSet()),
                athlete.getEmailPermission(),
                athlete.getPhonePermission()
        );
        List<AthleteResponseDto> responseList = List.of(responseDto);

        AthleteFilterRequestDto filter = new AthleteFilterRequestDto(
                "John",
                null,
                Gender.MALE,
                null,
                Set.of(1L),
                true,
                null
        );

        // When
        when(athleteService.getAllAthlete(any(AthleteFilterRequestDto.class), any(Pageable.class)))
                .thenReturn(responseList);

        // Then
        mockMvc.perform(get("/athlete")
                        .param("firstName", "John")
                        .param("gender", "MALE")
                        .param("sportIds", "1")
                        .param("emailPermission", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user("admin@example.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(athlete.getId()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].phoneNumbers[0]").value("+38(050)-123-4567"))
                .andExpect(jsonPath("$[0].sportIds[0]").value(1));
    }

    @Test
    void getAllAthlete_NonAdmin_403() throws Exception {
        // When and Then
        mockMvc.perform(get("/athlete")
                        .with(user("john.doe@example.com").roles("ATHLETE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAthleteById_Self_Success() throws Exception {
        // Given
        AthleteUpdateRequestDto updateRequestDto = new AthleteUpdateRequestDto(
                "John",
                "Smith",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/new-image.jpg",
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                false,
                true
        );

        AthleteResponseDto responseDto = new AthleteResponseDto(
                athlete.getId(),
                "John",
                "Smith",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/new-image.jpg",
                athlete.getUserCredentials().getEmail(),
                athlete.getUserCredentials().getUserType().name(),
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                false,
                true
        );

        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(true);
        when(athleteService.updateAthleteByAuth(any(), any(AthleteUpdateRequestDto.class)))
                .thenReturn(responseDto);

        // Then
        mockMvc.perform(patch("/athlete/{id}", athlete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .with(user("john.doe@example.com").roles("ATHLETE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(athlete.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(050)-987-6543"))
                .andExpect(jsonPath("$.sportIds[0]").value(1))
                .andExpect(jsonPath("$.emailPermission").value(false))
                .andExpect(jsonPath("$.phonePermission").value(true));
    }

    @Test
    void updateAthleteById_Unauthorized_403() throws Exception {
        // Given
        AthleteUpdateRequestDto updateRequestDto = new AthleteUpdateRequestDto(
                "John",
                "Smith",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/new-image.jpg",
                Set.of("+38(050)-987-6543"),
                Set.of(1L),
                false,
                true
        );

        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(patch("/athlete/{id}", athlete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .with(user("other.user@example.com").roles("ATHLETE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAthleteById_Admin_Success() throws Exception {
        // When
        doNothing().when(athleteService).deleteAthleteById(eq(athlete.getId()));

        // Then
        mockMvc.perform(delete("/athlete/{id}", athlete.getId())
                        .with(user("admin@example.com").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAthleteById_Self_Success() throws Exception {
        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(true);
        doNothing().when(athleteService).deleteAthleteById(eq(athlete.getId()));

        // Then
        mockMvc.perform(delete("/athlete/{id}", athlete.getId())
                        .with(user("john.doe@example.com").roles("ATHLETE")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAthleteById_Unauthorized_403() throws Exception {
        // When
        when(athleteService.canUserModifyAthlete(any(), eq(athlete.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(delete("/athlete/{id}", athlete.getId())
                        .with(user("other.user@example.com").roles("ATHLETE")))
                .andExpect(status().isForbidden());
    }
}
