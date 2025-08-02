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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.service.SportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SportService sportService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllSports_Success() throws Exception {
        // Given
        SportDto sportDto = new SportDto(1L, "Yoga");
        Pageable pageable = PageRequest.of(0, 10);
        Page<SportDto> sportPage = new PageImpl<>(List.of(sportDto), pageable, 1);
        when(sportService.getAllSports(any(SportDto.class), eq(pageable)))
                .thenReturn(sportPage.getContent());

        // Then
        mockMvc.perform(get("/sport")
                        .param("name", "Yoga")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sportName").value("Yoga"))
                .andDo(print());

        verify(sportService).getAllSports(any(SportDto.class), eq(pageable));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSport_Success() throws Exception {
        // Given
        SportRequestDto requestDto = new SportRequestDto("Yoga");
        SportDto responseDto = new SportDto(1L, "Yoga");
        when(sportService.createSport(any(SportRequestDto.class))).thenReturn(responseDto);

        // Then
        mockMvc.perform(post("/sport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sportName").value("Yoga"))
                .andDo(print());

        verify(sportService).createSport(any(SportRequestDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSport_InvalidDto_400() throws Exception {
        // Given
        SportRequestDto invalidDto = new SportRequestDto("");

        // Then
        MvcResult result = mockMvc.perform(post("/sport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "sportName: must not be empty"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSport_Success() throws Exception {
        // Given
        SportRequestDto requestDto = new SportRequestDto("Updated Yoga");
        SportDto responseDto = new SportDto(1L, "Updated Yoga");
        when(sportService.updateSport(eq(1L), any(SportRequestDto.class))).thenReturn(responseDto);

        // Then
        mockMvc.perform(patch("/sport/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sportName").value("Updated Yoga"))
                .andDo(print());

        verify(sportService).updateSport(eq(1L), any(SportRequestDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSport_InvalidId_400() throws Exception {
        // Given
        SportRequestDto requestDto = new SportRequestDto("Yoga");

        // Then
        MvcResult result = mockMvc.perform(patch("/sport/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "updateSport.id: must be greater than 0"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSport_NotFound_404() throws Exception {
        // Given
        SportRequestDto requestDto = new SportRequestDto("Yoga");
        when(sportService.updateSport(eq(999L), any(SportRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Can not find Sport by id: 999"));

        // Then
        MvcResult result = mockMvc.perform(patch("/sport/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Can not find Sport by id: 999"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSport_DuplicateName_400() throws Exception {
        // Given
        SportRequestDto requestDto = new SportRequestDto("Yoga");
        when(sportService.updateSport(eq(1L), any(SportRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Sport with name Yoga already exists"));

        // Then
        MvcResult result = mockMvc.perform(patch("/sport/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Sport with name Yoga already exists"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSport_Success() throws Exception {
        // Given
        doNothing().when(sportService).deleteSport(eq(1L));

        // Then
        mockMvc.perform(delete("/sport/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(sportService).deleteSport(eq(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSport_NotFound_404() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Can not find Sport by id: 999"))
                .when(sportService).deleteSport(eq(999L));

        // Then
        MvcResult result = mockMvc.perform(delete("/sport/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*]").value(hasItems(
                        "Can not find Sport by id: 999"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    void getAllSports_PublicAccess_200() throws Exception {
        // Then
        mockMvc.perform(get("/sport")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }
}
