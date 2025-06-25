package com.example.trainup.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.dto.chat.ChatRequestDto;
import com.example.trainup.model.ChatSession;
import com.example.trainup.service.GeminiChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GeminiChatService geminiChatService;

    @MockBean
    private ChatSession chatSession;

    private ChatRequestDto validChatRequestDto;
    private ChatMessageDto userMessage;
    private ChatMessageDto aiMessage;

    @BeforeEach
    void setup() {
        validChatRequestDto = new ChatRequestDto(
                "What is the best workout plan?",
                Optional.of(false),
                "Act as a fitness coach"
        );

        userMessage = new ChatMessageDto("user", "What is the best workout plan?");
        aiMessage = new ChatMessageDto("assistant",
                "A balanced workout plan includes strength training, cardio, "
                        + "and flexibility exercises.");
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void askAi_NewConversation_Success() throws Exception {
        // Given
        ChatRequestDto requestDto = new ChatRequestDto(
                "What is the best workout plan?",
                Optional.of(true),
                "Act as a fitness coach"
        );
        List<ChatMessageDto> responseHistory = List.of(
                new ChatMessageDto("system", "Act as a fitness coach"),
                userMessage,
                aiMessage
        );

        // When
        when(geminiChatService.startNewConversation(anyString(), anyString()))
                .thenReturn(responseHistory);

        // Then
        mockMvc.perform(post("/chat/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("system"))
                .andExpect(jsonPath("$[0].content").value("Act as a fitness coach"))
                .andExpect(jsonPath("$[1].role").value("user"))
                .andExpect(jsonPath("$[1].content").value("What is the best workout plan?"))
                .andExpect(jsonPath("$[2].role").value("assistant"))
                .andExpect(jsonPath("$[2].content").value("A balanced workout plan includes "
                        + "strength training, cardio, and flexibility exercises."));
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void askAi_ContinueConversation_Success() throws Exception {
        // Given
        List<ChatMessageDto> responseHistory = List.of(userMessage, aiMessage);

        // When
        when(geminiChatService.continueConversation(anyString())).thenReturn(responseHistory);

        // Then
        mockMvc.perform(post("/chat/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validChatRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("user"))
                .andExpect(jsonPath("$[0].content").value("What is the best workout plan?"))
                .andExpect(jsonPath("$[1].role").value("assistant"))
                .andExpect(jsonPath("$[1].content").value("A balanced workout plan includes "
                        + "strength training, cardio, and flexibility exercises."));
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void askAi_EmptyQuestion_400() throws Exception {
        // Given
        ChatRequestDto invalidDto = new ChatRequestDto(
                "",
                Optional.of(false),
                "Act as a fitness coach"
        );

        // Then
        MvcResult result = mockMvc.perform(post("/chat/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("question: must not be blank"))
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void askAi_GeminiError_500() throws Exception {
        // Given
        when(geminiChatService.continueConversation(anyString()))
                .thenThrow(new RuntimeException("Failed to process Gemini request"));

        // Then
        MvcResult result = mockMvc.perform(post("/chat/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validChatRequestDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value("An unexpected error occurred"))
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getChatHistory_Success() throws Exception {
        // Given
        List<ChatMessageDto> history = List.of(userMessage, aiMessage);

        // When
        when(chatSession.getMessageHistory()).thenReturn(history);

        // Then
        mockMvc.perform(get("/chat/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("user"))
                .andExpect(jsonPath("$[0].content").value("What is the best workout plan?"))
                .andExpect(jsonPath("$[1].role").value("assistant"))
                .andExpect(jsonPath("$[1].content").value("A balanced workout plan includes "
                        + "strength training, cardio, and flexibility exercises."));
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void getChatHistory_EmptyHistory_Success() throws Exception {
        // Given
        when(chatSession.getMessageHistory()).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/chat/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "ATHLETE")
    void clearChatHistory_Success() throws Exception {
        // Given
        doNothing().when(chatSession).clearHistory();

        // Then
        mockMvc.perform(post("/chat/clear-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Chat history cleared successfully."));
    }
}
