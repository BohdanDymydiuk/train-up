package com.example.trainup.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

public record ChatRequestDto(
        @NotBlank
        String question,

        @JsonProperty
        Optional<Boolean> newConversation,

        String customPrompt
) {
}
