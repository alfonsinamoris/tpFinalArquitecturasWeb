package org.example.chat.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class TimeService {
    @Tool(name = "argentinaTime", description = "Get the current time for Argentina")
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
