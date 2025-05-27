package com.example.trainup.controller;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.dto.chat.ChatRequestDto;
import com.example.trainup.model.ChatSession;
import com.example.trainup.service.GeminiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "AI Chat Functions",
        description = "Endpoints for interacting with the AI chat model and managing chat history."
)
public class ChatController {
    private final GeminiChatService geminiChatService;
    private final ChatSession chatSession;

    @PostMapping("/ask")
    @Operation(
            summary = "Ask AI a question",
            description = "Sends a question to the AI model. Supports starting new conversations "
                    + "or continuing existing ones."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received AI response and chat history."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request: Invalid input or prompt file issues."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error: An error occurred while communicating "
                            + "with the AI model or processing the request."
            )
    })
    public ResponseEntity<List<ChatMessageDto>> askAi(@RequestBody ChatRequestDto requestDto) {
        log.info("Received AI chat request: {}", requestDto);

        List<ChatMessageDto> responseHistory;
        if (requestDto.newConversation()) {
            // Починаємо нову розмову, історія буде очищена в ChatSession
            log.info("Starting a new conversation. Chat history will be cleared.");
            responseHistory = geminiChatService
                    .startNewConversation(requestDto.question(), requestDto.customPrompt());
        } else {
            // Продовжуємо існуючу розмову
            log.info("Continuing existing conversation.");
            responseHistory = geminiChatService
                    .continueConversation(requestDto.question()); // Тепер без передачі історії
        }

        // ChatSession вже керує історією
        log.info("AI response processed. Current chat history size: {}", responseHistory.size());
        return ResponseEntity.ok(responseHistory);
    }

    @GetMapping("/history") // Без PathVariable, бо історія прив'язана до поточної сесії
    @Operation(
            summary = "Get Chat AI history",
            description = "Retrieves the full chat history for the current session."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chat history.")
    })
    public ResponseEntity<List<ChatMessageDto>> getChatHistory() {
        log.info("Attempting to retrieve chat history for current session.");
        List<ChatMessageDto> history = chatSession.getMessageHistory();

        log.info("Successfully retrieved chat history. Number of messages: {}", history.size());
        return ResponseEntity.ok(history);
    }

    @PostMapping("/clear-history")
    @Operation(
            summary = "Clear Chat AI history",
            description = "Clears all messages from the current chat session's history."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat history successfully cleared.")
    })
    public ResponseEntity<String> clearChatHistory() {
        log.info("Attempting to clear chat history for current session.");
        chatSession.clearHistory(); // Викликаємо метод на ChatSession

        log.info("Chat history for current session cleared successfully.");
        return ResponseEntity.ok("Chat history cleared successfully.");
    }
}
