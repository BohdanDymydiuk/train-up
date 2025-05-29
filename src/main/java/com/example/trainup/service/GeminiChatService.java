package com.example.trainup.service;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.model.ChatSession;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
        if (chatSession.getMessageHistory().isEmpty()) {
            chatSession.getMessageHistory().add(new ChatMessageDto("system", systemPrompt));
        }

        chatSession.getMessageHistory().add(new ChatMessageDto("user", newUserMessage));
        return sendToGemini(chatSession.getMessageHistory());
    }

    private List<ChatMessageDto> sendToGemini(List<ChatMessageDto> chatHistory) {
        try {
            List<String> messages = chatHistory.stream()
                    .map(msg -> msg.role() + ": " + msg.content())
                    .toList();

            GenerateContentResponse response = geminiClient.models.generateContent(
                    geminiModelName,
                    String.join("\n", messages),
                    null
            );

            List<ChatMessageDto> currentChatHistory = new ArrayList<>(chatHistory);
            if (response != null && response.text() != null) {
                ChatMessageDto aiResponse = new ChatMessageDto("assistant", response.text());
                currentChatHistory.add(aiResponse);
                chatSession.getMessageHistory().add(aiResponse);
                log.info("Received response from Gemini API: {}", response.text());
            } else {
                log.warn("Empty or invalid response from Gemini API.");
                throw new RuntimeException("Failed to get response from Gemini.");
            }

            return currentChatHistory;
        } catch (Exception e) {
            log.error("Error communicating with Gemini API", e);
            throw new RuntimeException("Failed to process Gemini request", e);
        }
    }
}
