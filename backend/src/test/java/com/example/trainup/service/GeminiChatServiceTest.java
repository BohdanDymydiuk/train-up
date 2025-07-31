package com.example.trainup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.model.ChatSession;
import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@ExtendWith(MockitoExtension.class)
class GeminiChatServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private ChatSession chatSession;

    @Mock
    private Client geminiClient;

    @Mock
    private Resource resource;

    @InjectMocks
    private GeminiChatService geminiChatService;

    private String systemPromptFile = "classpath:prompt.txt";
    private String geminiModelName = "gemini-2.0-flash";
    private String systemPromptContent = "You are a helpful assistant.";
    private ChatMessageDto systemMessage;
    private ChatMessageDto userMessage;
    private ChatMessageDto assistantMessage;
    private List<ChatMessageDto> messageHistory;

    @BeforeEach
    void setUp() throws Exception {
        systemMessage = new ChatMessageDto("system", systemPromptContent);
        userMessage = new ChatMessageDto("user", "Hello, how are you?");
        assistantMessage = new ChatMessageDto("assistant", "I'm doing great, thanks!");

        messageHistory = new ArrayList<>();
        messageHistory.add(systemMessage);
        messageHistory.add(userMessage);

        Field systemPromptFileField = GeminiChatService.class.getDeclaredField("systemPromptFile");
        systemPromptFileField.setAccessible(true);
        systemPromptFileField.set(geminiChatService, systemPromptFile);

        var geminiModelNameField = GeminiChatService.class.getDeclaredField("geminiModelName");
        geminiModelNameField.setAccessible(true);
        geminiModelNameField.set(geminiChatService, geminiModelName);
    }

    @Test
    void loadSystemPrompt_Success() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(systemPromptContent
                .getBytes(StandardCharsets.UTF_8));
        when(resourceLoader.getResource(systemPromptFile)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(inputStream);

        // When
        geminiChatService.loadSystemPrompt();

        // Then
        verify(resourceLoader).getResource(systemPromptFile);
        verify(resource).getInputStream();
        assertEquals(systemPromptContent, getPrivateField(geminiChatService, "systemPrompt"));
        verifyNoInteractions(chatSession, geminiClient);
    }

    @Test
    void loadSystemPrompt_IoException_ThrowsIllegalStateException() throws IOException {
        // Given
        when(resourceLoader.getResource(systemPromptFile)).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException("File not found"));

        // When/Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> geminiChatService.loadSystemPrompt());
        assertEquals("Cannot start without system prompt", exception.getMessage());
        verify(resourceLoader).getResource(systemPromptFile);
        verify(resource).getInputStream();
        verifyNoInteractions(chatSession, geminiClient);
    }

    @Test
    void startNewConversation_WithSystemPrompt_Success() {
        // Given
        String userQuestion = "Hello, how are you?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userQuestion
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(assistantMessage.content());
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        doAnswer(invocation -> {
            messageHistory.clear();
            return null;
        }).when(chatSession).clearHistory();
        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When
        List<ChatMessageDto> result = geminiChatService.startNewConversation(userQuestion, null);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new ChatMessageDto("system", systemPromptContent), result.get(0));
        assertEquals(new ChatMessageDto("user", userQuestion), result.get(1));
        assertEquals(assistantMessage, result.get(2));
        verify(chatSession).clearHistory();
        verify(chatSession, times(3)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void startNewConversation_WithCustomPrompt_Success() {
        // Given
        String userQuestion = "Hello, how are you?";
        String customPrompt = "Custom assistant prompt.";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + customPrompt,
                "user: " + userQuestion
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(assistantMessage.content());
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        doAnswer(invocation -> {
            messageHistory.clear();
            return null;
        }).when(chatSession).clearHistory();
        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When
        List<ChatMessageDto> result = geminiChatService
                .startNewConversation(userQuestion, customPrompt);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new ChatMessageDto("system", customPrompt), result.get(0));
        assertEquals(new ChatMessageDto("user", userQuestion), result.get(1));
        assertEquals(assistantMessage, result.get(2));
        verify(chatSession).clearHistory();
        verify(chatSession, times(3)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void startNewConversation_GeminiApiFails_ThrowsRuntimeException() {
        // Given
        String userQuestion = "Hello, how are you?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userQuestion
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenThrow(new RuntimeException("API error"));

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        doAnswer(invocation -> {
            messageHistory.clear();
            return null;
        }).when(chatSession).clearHistory();
        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geminiChatService.startNewConversation(userQuestion, null));
        assertEquals("Failed to process Gemini request", exception.getMessage());
        verify(chatSession).clearHistory();
        verify(chatSession, times(3)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void startNewConversation_EmptyGeminiResponse_ThrowsRuntimeException() {
        // Given
        String userQuestion = "Hello, how are you?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userQuestion
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(null);
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        doAnswer(invocation -> {
            messageHistory.clear();
            return null;
        }).when(chatSession).clearHistory();
        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geminiChatService.startNewConversation(userQuestion, null));
        assertEquals("Failed to process Gemini request", exception.getMessage());
        verify(chatSession).clearHistory();
        verify(chatSession, times(3)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void continueConversation_NewSession_Success() {
        // Given
        String newUserMessage = "What's the weather like?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + newUserMessage
        ));

        when(chatSession.getMessageHistory()).thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(List.of(
                        new ChatMessageDto("system", systemPromptContent),
                        new ChatMessageDto("user", newUserMessage),
                        new ChatMessageDto("assistant", assistantMessage.content())
                )));

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(assistantMessage.content());
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When
        List<ChatMessageDto> result = geminiChatService.continueConversation(newUserMessage);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new ChatMessageDto("system", systemPromptContent), result.get(0));
        assertEquals(new ChatMessageDto("user", newUserMessage), result.get(1));
        assertEquals(assistantMessage, result.get(2));
        verify(chatSession, times(1)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void continueConversation_ExistingSession_Success() {
        // Given
        String newUserMessage = "What's the weather like?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userMessage.content(),
                "user: " + newUserMessage
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(assistantMessage.content());
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When
        List<ChatMessageDto> result = geminiChatService.continueConversation(newUserMessage);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(systemMessage, result.get(0));
        assertEquals(userMessage, result.get(1));
        assertEquals(new ChatMessageDto("user", newUserMessage), result.get(2));
        verify(chatSession, times(1)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void continueConversation_GeminiApiFails_ThrowsRuntimeException() {
        // Given
        String newUserMessage = "What's the weather like?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userMessage.content(), // Попереднє повідомлення
                "user: " + newUserMessage
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenThrow(new RuntimeException("API error"));

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geminiChatService.continueConversation(newUserMessage));
        assertEquals("Failed to process Gemini request", exception.getMessage());
        verify(chatSession, times(1)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    @Test
    void continueConversation_EmptyGeminiResponse_ThrowsRuntimeException() {
        // Given
        String newUserMessage = "What's the weather like?";
        String expectedPrompt = String.join("\n", List.of(
                "system: " + systemPromptContent,
                "user: " + userMessage.content(), // Попереднє повідомлення
                "user: " + newUserMessage
        ));

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        Models models = mock(Models.class);
        GenerateContentResponse response = mock(GenerateContentResponse.class);
        when(response.text()).thenReturn(null); // Порожня відповідь
        when(models.generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null)))
                .thenReturn(response);

        try {
            Field modelsField = Client.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            modelsField.set(geminiClient, models);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set mock models field in geminiClient", e);
        }

        when(chatSession.getMessageHistory()).thenReturn(messageHistory);

        setPrivateField(geminiChatService, "systemPrompt", systemPromptContent);

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> geminiChatService.continueConversation(newUserMessage));
        assertEquals("Failed to process Gemini request", exception.getMessage());
        verify(chatSession, times(1)).getMessageHistory();
        verify(models).generateContent(eq(geminiModelName), eq(expectedPrompt), eq(null));
        verifyNoInteractions(resourceLoader, resource);
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = GeminiChatService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set private field: " + fieldName, e);
        }
    }

    private String getPrivateField(Object target, String fieldName) {
        try {
            var field = GeminiChatService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get private field: " + fieldName, e);
        }
    }
}
