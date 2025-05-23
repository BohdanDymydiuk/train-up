package com.example.trainup.controller;

import com.example.trainup.dto.chat.ChatMessageDto;
import com.example.trainup.dto.chat.ChatRequestDto;
import com.example.trainup.model.ChatSession;
import com.example.trainup.service.ChatGptService;
import com.plexpt.chatgpt.entity.chat.Message;
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
public class ChatController {
    private final ChatGptService chatGptService;
    private final ChatSession chatSession;

    @PostMapping("/ask")
    public ResponseEntity<String> askChatGpt(@RequestBody ChatRequestDto request) {
        try {
            List<Message> updatedHistory;

            if (chatSession.getMessageHistory().isEmpty() || request.newConversation()) {
                log.info("Starting a new conversation with a question: {}", request.question());
                updatedHistory = chatGptService
                        .startNewConversation(request.question(), request.customPrompt());
            } else {
                log.info("Continuing the conversation with a question: {}", request.question());
                updatedHistory = chatGptService.continueConversation(
                        chatSession.getMessageHistory(), request.question());
            }

            chatSession.setMessageHistory(updatedHistory);
            String aiResponse = updatedHistory.get(updatedHistory.size() - 1).getContent();
            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            log.error("ChatGPT request processing error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory() {
        List<ChatMessageDto> history = chatSession.getMessageHistory().stream()
                .filter(m -> !"system".equals(m.getRole()))
                .map(m -> new ChatMessageDto(m.getRole(), m.getContent()))
                .toList();
        return ResponseEntity.ok(history);
    }

    @PostMapping("/clear-history")
    public ResponseEntity<Void> clearChatHistory() {
        chatSession.clearHistory();
        log.info("Chat history cleared for the session.");
        return ResponseEntity.noContent().build();
    }
}
