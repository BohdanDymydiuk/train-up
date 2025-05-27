package com.example.trainup.dto.gemini;

import com.example.trainup.dto.chat.ChatMessageDto;
import java.util.List;

// Відповідає GenerateContentResponse
public record GeminiResponse(
        List<GeminiCandidate> candidates
) {
    // Метод для конвертації в ваш ChatMessageDto
    public List<ChatMessageDto> toChatMessageDtos() { // Використовуйте ваш ChatMessageDto
        return candidates.stream()
                .filter(c -> c.content() != null && !c.content().parts().isEmpty())
                .map(c -> {
                    String role = c.content().role() != null ? c.content().role() : "assistant";
                    // Зазвичай "model"
                    // ЗМІНА ТУТ: Викликаємо .text() на GeminiPart
                    String text = c.content().parts().get(0).text();
                    return new ChatMessageDto(role, text); // Використовуйте ваш ChatMessageDto
                })
                .toList();
    }
}
