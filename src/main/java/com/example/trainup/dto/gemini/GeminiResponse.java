package com.example.trainup.dto.gemini;

import com.example.trainup.dto.chat.ChatMessageDto;
import java.util.List;

public record GeminiResponse(
        List<GeminiCandidate> candidates
) {
    public List<ChatMessageDto> toChatMessageDtos() {
        return candidates.stream()
                .filter(c -> c.content() != null && !c.content().parts().isEmpty())
                .map(c -> {
                    String role = c.content().role() != null ? c.content().role() : "assistant";
                    // Зазвичай "model"
                    // ЗМІНА ТУТ: Викликаємо .text() на GeminiPart
                    String text = c.content().parts().get(0).text();
                    return new ChatMessageDto(role, text);
                })
                .toList();
    }
}
