package com.example.trainup.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.review.ReviewFilterRequestDto;
import com.example.trainup.dto.review.ReviewRegistrationRequestDto;
import com.example.trainup.dto.review.ReviewResponseDto;
import com.example.trainup.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
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
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "reviewServiceImpl")
    private ReviewService reviewService;

    private final ReviewRegistrationRequestDto validReviewDto = new ReviewRegistrationRequestDto(
            5,
            "Great gym with excellent facilities!"
    );

    private final ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
            1L,
            5,
            "Great gym with excellent facilities!",
            1L,
            null,
            1L
    );

    @Test
    @WithMockUser(username = "ATHLETE")
    void getAllReview_Success() throws Exception {
        // Given
        ReviewFilterRequestDto filter = new ReviewFilterRequestDto(1L, 5, 1L, 1L, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseDto> page = new PageImpl<>(Collections.singletonList(reviewResponseDto));
        when(reviewService.getAllReviews(any(ReviewFilterRequestDto.class), eq(pageable)))
                .thenReturn(page.getContent());

        // Then
        mockMvc.perform(get("/review")
                        .param("id", "1")
                        .param("rating", "5")
                        .param("authorId", "1")
                        .param("gymId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].description")
                        .value("Great gym with excellent facilities!"))
                .andExpect(jsonPath("$[0].gymId").value(1L))
                .andExpect(jsonPath("$[0].trainerId").doesNotExist())
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getAllReview_InvalidRating_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(get("/review")
                        .param("rating", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteReviewById_Success() throws Exception {
        // Given
        doNothing().when(reviewService).deleteById(eq(1L));

        // Then
        MvcResult result = mockMvc.perform(delete("/review/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteReviewById_NotFound_404() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Can not find Review by id: 1")).when(reviewService)
                .deleteById(eq(1L));

        // Then
        MvcResult result = mockMvc.perform(delete("/review/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$..errors[0]").value("Can not find Review by id: 1"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void deleteReviewById_Unauthenticated_403() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(delete("/review/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createGymReview_Success() throws Exception {
        // Given
        when(reviewService.createGymReview(any(), eq(1L), any(ReviewRegistrationRequestDto.class)))
                .thenReturn(reviewResponseDto);

        // Then
        MvcResult result = mockMvc.perform(post("/review/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.description").value("Great gym with excellent facilities!"))
                .andExpect(jsonPath("$.gymId").value(1L))
                .andExpect(jsonPath("$.trainerId").doesNotExist())
                .andExpect(jsonPath("$.authorId").value(1L))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createGymReview_InvalidGymId_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/review/gym/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasItem(
                        "createGymReview.gymId: must be greater than 0")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createGymReview_InvalidDto_400() throws Exception {
        // Given
        ReviewRegistrationRequestDto invalidDto = new ReviewRegistrationRequestDto(
                0,
                "Short"
        );

        // Then
        MvcResult result = mockMvc.perform(post("/review/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").value(hasItems(
                        "rating: The rating must be at least 1",
                        "description: The description must be at least 10 characters"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    void createGymReview_Unauthenticated_403() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/review/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createGymReview_GymNotFound_404() throws Exception {
        // Given
        when(reviewService.createGymReview(any(), eq(1L), any(ReviewRegistrationRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Gym not found with id: 1"));

        // Then
        MvcResult result = mockMvc.perform(post("/review/gym/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$..errors[0]").value("Gym not found with id: 1"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createTrainerReview_Success() throws Exception {
        // Given
        ReviewResponseDto trainerReviewDto = new ReviewResponseDto(
                1L,
                5,
                "Great trainer, very professional!",
                null,
                1L,
                1L
        );
        when(reviewService.createTrainerReview(
                any(),
                eq(1L),
                any(ReviewRegistrationRequestDto.class))
        ).thenReturn(trainerReviewDto);

        // Then
        MvcResult result = mockMvc.perform(post("/review/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.description").value("Great trainer, very professional!"))
                .andExpect(jsonPath("$.gymId").doesNotExist())
                .andExpect(jsonPath("$.trainerId").value(1L))
                .andExpect(jsonPath("$.authorId").value(1L))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createTrainerReview_InvalidTrainerId_400() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/review/trainer/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasItem(
                        "createTrainerReview.trainerId: must be greater than 0")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createTrainerReview_InvalidDto_400() throws Exception {
        // Given
        ReviewRegistrationRequestDto invalidDto = new ReviewRegistrationRequestDto(
                0,
                "Short"
        );

        // Then
        MvcResult result = mockMvc.perform(post("/review/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasItems(
                        "rating: The rating must be at least 1",
                        "description: The description must be at least 10 characters"
                )))
                .andDo(print())
                .andReturn();
    }

    @Test
    void createTrainerReview_Unauthenticated_403() throws Exception {
        // Then
        MvcResult result = mockMvc.perform(post("/review/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createTrainerReview_TrainerNotFound_404() throws Exception {
        // Given
        when(reviewService.createTrainerReview(
                any(),
                eq(1L),
                any(ReviewRegistrationRequestDto.class))
        ).thenThrow(new EntityNotFoundException("Trainer not found with id: 1"));

        // Then
        MvcResult result = mockMvc.perform(post("/review/trainer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$..errors[0]").value("Trainer not found with id: 1"))
                .andDo(print())
                .andReturn();
    }
}
