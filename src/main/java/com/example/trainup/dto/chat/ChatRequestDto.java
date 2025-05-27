package com.example.trainup.dto.chat;

public record ChatRequestDto(
        String question,
        Boolean newConversation,
        String customPrompt
) {
}
