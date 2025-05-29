package com.example.trainup.dto.gemini;

import java.util.List;

public record GeminiContent(String role, List<GeminiPart> parts) {
}
