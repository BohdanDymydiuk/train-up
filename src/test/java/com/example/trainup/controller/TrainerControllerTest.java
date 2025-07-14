package com.example.trainup.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.users.trainer.TrainerFilterRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.dto.users.trainer.TrainerUpdateRequestDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.service.users.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@ActiveProfiles("test")
class TrainerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    private TrainerResponseDto trainerResponseDto;
    private TrainerUpdateRequestDto trainerUpdateRequestDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        trainerResponseDto = new TrainerResponseDto(
                1L,
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                "trainer@example.com",
                "TRAINER",
                Set.of("123456789"),
                Set.of(1L),
                Set.of(1L),
                null,
                "Experienced trainer",
                "http://social.com",
                4.5f,
                10
        );

        trainerUpdateRequestDto = new TrainerUpdateRequestDto(
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                "http://example.com/image.jpg",
                Set.of("123456789"),
                Set.of(1L),
                Set.of(1L),
                null,
                true,
                List.of("Certificate"),
                "Updated description",
                "http://social.com"
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @WithMockUser(username = "ATHLETE")
    void getAllTrainers_Success() throws Exception {
        // Given
        Page<TrainerResponseDto> trainerPage = new PageImpl<>(
                List.of(trainerResponseDto),
                pageable,
                1
        );
        when(trainerService.getAllTrainers(any(TrainerFilterRequestDto.class), eq(pageable)))
                .thenReturn(trainerPage.getContent());

        // Then
        mockMvc.perform(get("/trainer")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("trainer@example.com"))
                .andDo(print());

        verify(trainerService).getAllTrainers(any(TrainerFilterRequestDto.class), eq(pageable));
    }

    @Test
    @WithMockUser(username = "trainer@example.com", roles = "TRAINER")
    void getCurrentTrainer_Success() throws Exception {
        // Given
        when(trainerService.getTrainerByAuth(any())).thenReturn(trainerResponseDto);

        // Then
        mockMvc.perform(get("/trainer/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("trainer@example.com"))
                .andDo(print());

        verify(trainerService).getTrainerByAuth(any());
    }

    @Test
    void getCurrentTrainer_Unauthorized_403() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(get("/trainer/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ATHLETE")
    void getTrainerById_Success() throws Exception {
        // Given
        when(trainerService.getTrainerById(1L)).thenReturn(trainerResponseDto);

        // Then
        mockMvc.perform(get("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("trainer@example.com"))
                .andDo(print());

        verify(trainerService).getTrainerById(1L);
    }

    @Test
    @WithMockUser(username = "ATHLETE")
    void getTrainerById_InvalidId_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(get("/trainer/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "getTrainerById.id: must be greater than 0"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ATHLETE")
    void getTrainerById_NotFound_404() throws Exception {
        // Given
        when(trainerService.getTrainerById(999L))
                .thenThrow(new EntityNotFoundException("Cannot find Trainer by id:999"));

        // Then
        MvcResult result = mockMvc.perform(get("/trainer/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Cannot find Trainer by id:999"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void deleteTrainerById_Success_Admin() throws Exception {
        // Given
        doNothing().when(trainerService).deleteTrainerById(1L);

        // Then
        mockMvc.perform(delete("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(trainerService).deleteTrainerById(1L);
    }

    @Test
    @WithMockUser(username = "trainer@example.com", roles = "TRAINER")
    void deleteTrainerById_Success_Self() throws Exception {
        // Given
        doNothing().when(trainerService).deleteTrainerById(1L);
        when(trainerService.canUserModifyTrainer(any(), eq(1L))).thenReturn(true);

        // Then
        mockMvc.perform(delete("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(trainerService).deleteTrainerById(1L);
        verify(trainerService).canUserModifyTrainer(any(), eq(1L));
    }

    @Test
    @WithMockUser(username = "trainer2@example.com", roles = "TRAINER")
    void deleteTrainerById_Unauthorized_403() throws Exception {
        // Given
        when(trainerService.canUserModifyTrainer(any(), eq(1L))).thenReturn(false);

        // Then
        MvcResult result = mockMvc.perform(delete("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void deleteTrainerById_NotFound_404() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Cannot find Trainer by id:999"))
                .when(trainerService).deleteTrainerById(999L);

        // Then
        MvcResult result = mockMvc.perform(delete("/trainer/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Cannot find Trainer by id:999"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "trainer@example.com", roles = "TRAINER")
    void updateTrainerById_Success() throws Exception {
        // Given
        when(trainerService.canUserModifyTrainer(any(), eq(1L))).thenReturn(true);
        when(trainerService.updateTrainerByAuth(any(), any(TrainerUpdateRequestDto.class)))
                .thenReturn(trainerResponseDto);

        // Then
        mockMvc.perform(patch("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.description").value("Experienced trainer"))
                .andDo(print());

        verify(trainerService).canUserModifyTrainer(any(), eq(1L));
        verify(trainerService).updateTrainerByAuth(any(), any(TrainerUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(username = "trainer@example.com", roles = "TRAINER")
    void updateTrainerById_Unauthorized_403() throws Exception {
        // Given
        when(trainerService.canUserModifyTrainer(any(), eq(1L))).thenReturn(false);

        // Then
        MvcResult result = mockMvc.perform(patch("/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUpdateRequestDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "trainer@example.com", roles = "TRAINER")
    void updateTrainerById_NotFound_404() throws Exception {
        // Given
        when(trainerService.canUserModifyTrainer(any(), eq(999L))).thenReturn(true);
        when(trainerService.updateTrainerByAuth(any(), any(TrainerUpdateRequestDto.class)))
                .thenThrow(new EntityNotFoundException(
                        "Trainer not found for email: trainer@example.com"));

        // Then
        MvcResult result = mockMvc.perform(patch("/trainer/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUpdateRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Trainer not found for email: trainer@example.com"
                )))
                .andDo(print())
                .andReturn();
    }
}
