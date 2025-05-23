package com.example.trainup.service;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletion.Model;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@Log4j2
public class ChatGptService {
    private ChatGPT chatGpt;
    private final String defaultSystemPrompt;

    public ChatGptService(
            @Value("${openai.api-key}") String apiKey,
            @Value("${chatgpt.proxy.host:}") String proxyHost,
            @Value("${chatgpt.proxy.port:0}") int proxyPort,
            @Value("${chatgpt.system-prompt-file:}") String systemPromptFilePath,
            ResourceLoader resourceLoader
    ) {
        Proxy proxy = null;
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != 0) {
            proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            log.info("Configuring ChatGPT with a proxy: {}:{}", proxyHost, proxyPort);
        } else {
            log.info("No proxy is configured for ChatGPT.");
        }

        this.chatGpt = ChatGPT.builder()
                .apiKey(apiKey)
                .apiHost("https://api.openai.com/")
                .proxy(proxy)
                .build()
                .init();
        this.defaultSystemPrompt = loadSystemPromptFromFile(systemPromptFilePath, resourceLoader);
        log.info("Loaded system prompt from file: {}", systemPromptFilePath);
    }

    /**
     * Починає нову розмову з даною системною підказкою та початковим запитанням.
     * Історія розмов повертається для керування викликаючою стороною.
     * @param question Початкове запитання користувача.
     * @param customPrompt Необов'язкова користувацька системна підказка для цієї розмови.
     *                     Якщо null, використовується за замовчуванням.
     * @return Оновлена історія повідомлень, включаючи першу відповідь ШІ.
     */
    public List<Message> startNewConversation(String question, String customPrompt) {
        List<Message> currentHistory = new ArrayList<>();
        currentHistory.add(Message.ofSystem(customPrompt != null && !customPrompt.isEmpty()
                ? customPrompt : defaultSystemPrompt));
        currentHistory.add(Message.of(question));

        return sendMessageToChatGpt(currentHistory);
    }

    /**
     * Продовжує існуючу розмову, додаючи нове повідомлення користувача.
     * Оновлена історія розмов повертається для керування викликаючою стороною.
     * @param existingHistory Поточна історія розмов.
     * @param question Нове повідомлення користувача.
     * @return Оновлена історія повідомлень, включаючи відповідь ШІ.
     * @throws IllegalArgumentException якщо existingHistory є null або порожнім.
     */
    public List<Message> continueConversation(List<Message> existingHistory, String question) {
        if (existingHistory == null || existingHistory.isEmpty()) {
            throw new IllegalArgumentException("Conversation history cannot be null or empty.");
        }
        // Створити змінну копію, щоб уникнути прямої зміни оригінального списку, якщо він незмінний
        List<Message> currentHistory = new ArrayList<>(existingHistory);
        currentHistory.add(Message.of(question));

        return sendMessageToChatGpt(currentHistory);
    }

    /**
     * Внутрішній метод для надсилання повідомлень до ChatGPT та обробки відповіді.
     * Приймає повну історію розмов для цього оберту.
     * @param conversationHistory Список повідомлень (включаючи системні,
     *                            користувацькі та повідомлення помічника).
     * @return Оновлена історія розмов, включаючи відповідь ШІ.
     * @throws RuntimeException якщо виклик API не вдається або відповідь недійсна.
     */
    private List<Message> sendMessageToChatGpt(List<Message> conversationHistory) {
        if (conversationHistory == null || conversationHistory.isEmpty()) {
            throw new IllegalArgumentException("The conversation history for an API call cannot "
                    + "be null or empty.");
        }

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(Model.GPT4oMini)
                .messages(conversationHistory) // Використовуйте передану історію
                .maxTokens(3000)
                .temperature(0.9)
                .build();

        try {
            ChatCompletionResponse response = chatGpt.chatCompletion(chatCompletion);

            if (response != null
                    && response.getChoices() != null
                    && !response.getChoices().isEmpty()
            ) {
                Message assistantResponse = response.getChoices().get(0).getMessage();
                conversationHistory.add(assistantResponse); // Додати відповідь ШІ до історії
                log.info("ChatGPT replied: {}", assistantResponse.getContent());
                return conversationHistory;
            } else {
                log.warn("ChatGPT returned an empty or invalid response.");
                throw new RuntimeException("Empty or invalid response from ChatGPT API.");
            }
        } catch (Exception e) {
            log.error("Error communicating with ChatGPT API: {}", e.getMessage(), e);
            throw new RuntimeException("Could not get a response from ChatGPT.", e);
        }
    }

    private String loadSystemPromptFromFile(String filePath, ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            try (Reader reader = new InputStreamReader(
                    resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (IOException e) {
            log.error("Failed to load system prompt from file");
            throw new RuntimeException(e);
        }
    }
}
