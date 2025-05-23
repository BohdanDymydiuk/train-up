package com.example.trainup.dto.chat;

public record ChatRequestDto(
        String question,
        String customPrompt,
        boolean newConversation
) {
}
