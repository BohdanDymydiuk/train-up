package com.example.trainup.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiClientConfig {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Bean
    public Client geminiClient() {
        return Client.builder()
                .apiKey(geminiApiKey)
                .build();
    }
}
