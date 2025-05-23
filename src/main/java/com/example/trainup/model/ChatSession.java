package com.example.trainup.model;

import com.plexpt.chatgpt.entity.chat.Message;
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
    private List<Message> messageHistory = new ArrayList<>();

    public void clearHistory() {
        messageHistory.clear();
    }
}
