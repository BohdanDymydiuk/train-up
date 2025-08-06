package com.example.trainup.dto.chat;

import java.io.Serializable;

public record ChatMessageDto(
        String role,
        String content
) implements Serializable {
}
