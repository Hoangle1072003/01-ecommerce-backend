package net.javaguides.chat_service.services.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.chat_service.services.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * File: ChatServiceImpl.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 20:07
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
public class AIServiceImpl implements AIService {
    @Value("${gemini.api.url}")
    private String geminiAPiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private final WebClient webClient;

    public AIServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public String askQuestion(String question) {
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", question)
                })
        });
        String response = webClient.post()
                .uri(geminiAPiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}
