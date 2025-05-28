package com.example.trainup.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatRequestDto(
        String question,
        @JsonProperty(defaultValue = "false") Boolean newConversation,
        String customPrompt
) {
}
