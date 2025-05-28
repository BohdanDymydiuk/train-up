package com.example.trainup.service;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.dto.gemini.GeminiContent;
import com.example.trainup.dto.gemini.GeminiPart;
import com.example.trainup.dto.gemini.GeminiRequest;
import com.example.trainup.dto.gemini.GeminiResponse;
import com.example.trainup.model.ChatSession;
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
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class GeminiChatService {
    private final WebClient geminiWebClient;
    private final ResourceLoader resourceLoader;
    private final ChatSession chatSession;
    private String systemPrompt;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.system-prompt-file}")
    private String systemPromptFile;

    @Value("${gemini.model.name}")
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

        chatSession.getMessageHistory().add(new ChatMessageDto("user", userQuestion));

        return sendToGemini(chatSession.getMessageHistory());
    }

    public List<ChatMessageDto> continueConversation(
            String newUserMessage
    ) {
        if (chatSession.getMessageHistory().isEmpty()) {
            // Автоматично добаємо systemPrompt, якщо нема історії
            chatSession.getMessageHistory().add(new ChatMessageDto("system", systemPrompt));
        }

        // Просто додаємо нове повідомлення до історії сесії
        chatSession.getMessageHistory().add(new ChatMessageDto("user", newUserMessage));
        return sendToGemini(chatSession.getMessageHistory());
    }

    private List<ChatMessageDto> sendToGemini(List<ChatMessageDto> chatHistory) {
        // 1. Конвертуємо наш внутрішній ChatMessageDto у GeminiContent, який потрібен API Gemini
        List<GeminiContent> geminiContents = chatHistory.stream()
                .map(msg -> new GeminiContent(
                        mapRoleToGemini(msg.role()), // Метод для мапінгу ролей
                        List.of(new GeminiPart(msg.content()))
                // Кожен TextPart має бути в List.of()
                ))
                .toList();

        // 2. Створюємо GeminiRequest (це тіло запиту до Gemini API)
        GeminiRequest request = new GeminiRequest(geminiContents);

        // 3. Відправляємо запит через WebClient
        // GeminiResponse - це те, що ми очікуємо назад від Gemini API
        log.info("Sending request to Gemini API: {}", request);

        GeminiResponse response = geminiWebClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models"
                        + "/gemini-pro:generateContent")
                .header("x-goog-api-key", geminiApiKey) // <-- ДОДАЄМО ВАШ API-КЛЮЧ ТУТ!
                .bodyValue(request) // Тіло запиту - це наш GeminiRequest
                .retrieve()
                .bodyToMono(GeminiResponse.class) // Очікуємо відповідь типу GeminiResponse
                    .block(); // Блокуємо виконання для отримання
        // відповіді (для асинхронних WebClient)

        // 4. Обробка відповіді та конвертація назад у ваш ChatMessageDto
        List<ChatMessageDto> currentChatHistory = new ArrayList<>(chatHistory);
        // Створюємо змінювану копію історії

        if (response != null) {
            List<ChatMessageDto> aiResponses = response.toChatMessageDtos();
            // Використовуємо метод з GeminiResponse
            currentChatHistory.addAll(aiResponses); // Додаємо відповіді AI до історії
            chatSession.getMessageHistory().addAll(aiResponses);
        } else {
            log.warn("Відповідь від Gemini API була порожньою або недійсною.");
            throw new RuntimeException("Не вдалося отримати відповідь від Gemini.");
        }
        return currentChatHistory; // Повертаємо оновлену історію
    }

    // Допоміжний метод для мапінгу ролей від вашого внутрішнього DTO до ролей Gemini API
    private String mapRoleToGemini(String role) {
        return switch (role) {
            case "user" -> "user";
            case "assistant" -> "model"; // Gemini використовує "model" для відповідей AI
            case "system" -> "user"; // Системні підказки відправляються
            // як повідомлення "user" до моделі
            default -> "user";
        };
    }
}
