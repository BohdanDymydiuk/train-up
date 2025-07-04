package com.example.trainup.service;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.model.ChatSession;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GeminiChatService {
    private final ResourceLoader resourceLoader;
    private final ChatSession chatSession;
    private final Client geminiClient;
    private String systemPrompt;

    @Value("${app.system-prompt-file}")
    private String systemPromptFile;

    @Value("${gemini.model.name:gemini-2.0-flash}")
    private String geminiModelName;

    @PostConstruct
    public void loadSystemPrompt() {
        try {
            Resource resource = resourceLoader.getResource(systemPromptFile);
            this.systemPrompt = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            log.info("System prompt loaded.");
        } catch (IOException e) {
            log.error("Failed to load system prompt", e);
            throw new IllegalStateException("Cannot start without system prompt", e);
        }
    }

    public List<ChatMessageDto> startNewConversation(String userQuestion, String customPrompt) {
        chatSession.clearHistory();
        String promptToUse = customPrompt != null && !customPrompt.isBlank()
                ? customPrompt : systemPrompt;

        chatSession.getMessageHistory().add(new ChatMessageDto("system", promptToUse));
        chatSession.getMessageHistory().add(new ChatMessageDto("user", userQuestion));

        return sendToGemini(chatSession.getMessageHistory());
    }

    public List<ChatMessageDto> continueConversation(String newUserMessage) {
        List<ChatMessageDto> currentChatHistory = chatSession.getMessageHistory();
        if (currentChatHistory.isEmpty()) {
            currentChatHistory.add(new ChatMessageDto("system", getSystemPrompt()));
        }
        currentChatHistory.add(new ChatMessageDto("user", newUserMessage));
        return sendToGemini(currentChatHistory);
    }

    private List<ChatMessageDto> sendToGemini(List<ChatMessageDto> chatHistory) {
        String prompt = chatHistory.stream()
                .map(msg -> msg.role() + ": " + msg.content())
                .collect(Collectors.joining("\n"));
        try {
            GenerateContentResponse response = geminiClient.models
                    .generateContent(geminiModelName, prompt, null);
            if (response.text() == null) {
                throw new RuntimeException("Failed to get response from Gemini.");
            }
            chatHistory.add(new ChatMessageDto("assistant", response.text()));
            return chatHistory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Gemini request", e);
        }
    }

    private String getSystemPrompt() {
        return (String) getPrivateField(this, "systemPrompt");
    }

    private Object getPrivateField(Object target, String fieldName) {
        try {
            var field = GeminiChatService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get private field: " + fieldName, e);
        }
    }
}
