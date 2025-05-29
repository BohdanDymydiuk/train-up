package com.example.trainup.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public record ChatRequestDto(
        String question,
        @JsonProperty Optional<Boolean> newConversation,
        String customPrompt
) {
}
