package com.example.trainup.model;

import com.example.trainup.dto.chat.ChatMessageDto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
public class ChatSession implements Serializable {
    private List<ChatMessageDto> messageHistory = new ArrayList<>();

    public void clearHistory() {
        messageHistory.clear();
    }
}
