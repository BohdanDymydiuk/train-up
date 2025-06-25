package com.example.trainup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.event.EventFilterRequestDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.dto.event.EventUpdateRequestDto;
import com.example.trainup.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    private EventRegistrationRequestDto validRegistrationDto;
    private EventResponseDto eventResponseDto;
    private EventUpdateRequestDto validUpdateDto;
    private EventFilterRequestDto filterDto;
    private Authentication authentication;

    @BeforeEach
    void setup() {
        validRegistrationDto = new EventRegistrationRequestDto(
                "Yoga Workshop",
                1L,
                "Beginner-friendly yoga session",
                LocalDateTime.now().plusDays(1)
        );

        eventResponseDto = new EventResponseDto(
                1L,
                "Yoga Workshop",
                1L,
                "Beginner-friendly yoga session",
                LocalDateTime.now().plusDays(1),
                null,
                1L
        );

        validUpdateDto = new EventUpdateRequestDto(
                "Advanced Yoga Workshop",
                2L,
                "Intermediate yoga session",
                LocalDateTime.now().plusDays(2)
        );

        filterDto = new EventFilterRequestDto(
                null,
                "Yoga",
                1L,
                LocalDate.now(),
                null,
                null
        );

        authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("trainer@example.com");
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void createEventByTrainer_Success() throws Exception {
        // Given
        when(eventService.createEventByTrainer(any(Authentication.class), any(EventRegistrationRequestDto.class)))
                .thenReturn(eventResponseDto);

        // Then
        MvcResult result = mockMvc.perform(post("/event/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Workshop"))
                .andExpect(jsonPath("$.sportId").value(1))
                .andExpect(jsonPath("$.description").value("Beginner-friendly yoga session"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 201) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void createEventByTrainer_InvalidDto_400() throws Exception {
        // Given
        EventRegistrationRequestDto invalidDto = new EventRegistrationRequestDto(
                "",
                null,
                "Description",
                null
        );

        // Then
        MvcResult result = mockMvc.perform(post("/event/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("name: must not be blank")))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("sportId: must not be null")))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("dateTime: Event dateTime cannot be null")))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 400) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void createEventByTrainer_TrainerNotFound_404() throws Exception {
        // Given
        when(eventService.createEventByTrainer(any(Authentication.class), any(EventRegistrationRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Trainer not found with email: trainer@example.com"));

        // Then
        MvcResult result = mockMvc.perform(post("/event/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Trainer not found with email: trainer@example.com"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 404) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "GYM_OWNER")
    void createEventByGymOwner_Success() throws Exception {
        // Given
        when(eventService.createEventByGymOwner(any(Authentication.class), any(EventRegistrationRequestDto.class), eq(1L)))
                .thenReturn(eventResponseDto);

        // Then
        MvcResult result = mockMvc.perform(post("/event/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Workshop"))
                .andExpect(jsonPath("$.sportId").value(1))
                .andExpect(jsonPath("$.description").value("Beginner-friendly yoga session"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 201) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "GYM_OWNER")
    void createEventByGymOwner_InvalidGymId_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/event/gym/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("gymId: must be greater than 0")))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 400) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "GYM_OWNER")
    void createEventByGymOwner_GymNotOwned_403() throws Exception {
        // Given
        when(eventService.createEventByGymOwner(any(Authentication.class), any(EventRegistrationRequestDto.class), eq(1L)))
                .thenThrow(new IllegalStateException("Gym does not belong to this owner"));

        // Then
        MvcResult result = mockMvc.perform(post("/event/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Gym does not belong to this owner"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 403) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getAllEvents_Success() throws Exception {
        // Given
        List<EventResponseDto> events = List.of(eventResponseDto);
        when(eventService.getAllEvents(any(EventFilterRequestDto.class), any(Pageable.class)))
                .thenReturn(events);

        // Then
        MvcResult result = mockMvc.perform(get("/event")
                        .param("name", "Yoga")
                        .param("sportId", "1")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Yoga Workshop"))
                .andExpect(jsonPath("$[0].sportId").value(1))
                .andExpect(jsonPath("$[0].description").value("Beginner-friendly yoga session"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 200) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getAllEvents_EmptyList_Success() throws Exception {
        // Given
        when(eventService.getAllEvents(any(EventFilterRequestDto.class), any(Pageable.class)))
                .thenReturn(List.of());

        // Then
        MvcResult result = mockMvc.perform(get("/event")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 200) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getAllEvents_InvalidSportId_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(get("/event")
                        .param("sportId", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("sportId: must be greater than 0")))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 400) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void updateEvent_Success() throws Exception {
        // Given
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(true);
        when(eventService.updateEvent(eq(1L), any(EventUpdateRequestDto.class))).thenReturn(eventResponseDto);

        // Then
        MvcResult result = mockMvc.perform(patch("/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Workshop"))
                .andExpect(jsonPath("$.sportId").value(1))
                .andExpect(jsonPath("$.description").value("Beginner-friendly yoga session"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 200) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void updateEvent_Unauthorized_403() throws Exception {
        // Given
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(false);

        // Then
        MvcResult result = mockMvc.perform(patch("/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 403) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void updateEvent_InvalidDateTime_400() throws Exception {
        // Given
        EventUpdateRequestDto invalidDto = new EventUpdateRequestDto(
                "Advanced Yoga",
                2L,
                "Intermediate session",
                LocalDateTime.now().minusDays(1)
        );
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(true);

        // Then
        MvcResult result = mockMvc.perform(patch("/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$..errors[*]").value(Matchers.containsInAnyOrder(
                        "dateTime: Event dateTime cannot be in the past"
                )))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 400) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void deleteEvent_Success() throws Exception {
        // Given
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(true);
        doNothing().when(eventService).deleteEvent(eq(1L));

        // Then
        MvcResult result = mockMvc.perform(delete("/event/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 204) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void deleteEvent_Unauthorized_403() throws Exception {
        // Given
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(false);

        // Then
        MvcResult result = mockMvc.perform(delete("/event/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 403) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void deleteEvent_NotFound_404() throws Exception {
        // Given
        when(eventService.canUserModifyEvent(eq("trainer@example.com"), eq(1L))).thenReturn(true);
        doThrow(new EntityNotFoundException("Event not found with id: 1")).when(eventService).deleteEvent(eq(1L));

        // Then
        MvcResult result = mockMvc.perform(delete("/event/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$..errors[0]").value("Event not found with id: 1"))
                .andReturn();

        // Діагностика
        if (result.getResponse().getStatus() != 404) {
            System.out.println("Response status: " + result.getResponse().getStatus());
            System.out.println("Response body: " + result.getResponse().getContentAsString());
        }
    }

    @Test
    void createEventByTrainer_Unauthenticated_403() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/event/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"))
                .andReturn();

        // Діагностика
        System.out.println("Response status: " + result.getResponse().getStatus());
        System.out.println("Response body: " + result.getResponse().getContentAsString());
    }
}
