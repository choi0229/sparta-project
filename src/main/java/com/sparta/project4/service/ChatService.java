package com.sparta.project4.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.project4.controller.dto.ChatRequest;
import com.sparta.project4.controller.dto.ChatResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final Map<String, ChatClient> chatClients; // spring이 모든 ChatClient Bean을 넣어줌
    private Map<String, ChatClient> chatClientMap = new HashMap<>();
    private final MessageConverter messageConverter;

    @PostConstruct
    public void init() {
        chatClientMap = chatClients.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replace("ChatClient", ""),  // ex anthropicChatClient -> anthropic으로 바꾸기
                        Map.Entry::getValue
                ));
        log.info("Initialized chat clients: {}", chatClientMap.keySet());
    }

    public ChatResponse chatSync(ChatRequest request) {
        List<Message> message = messageConverter.convertMessagesToSpringAI(request.messages());
        // ChatClient chatClient = chatClientMap.get(request.model());
        ChatClient chatClient = null;
        if(request.model().equals("qwen3")){
            chatClient = chatClientMap.get("ollama");
        }

        org.springframework.ai.chat.model.ChatResponse content = prompt(message, request, chatClient);
        ChatResponse response = messageConverter.convertMessageFromSpringAI(content, request.model());

        return response;
    }

    public Flux<ServerSentEvent<String>> chatStream(ChatRequest request) {
        List<Message> message = messageConverter.convertMessagesToSpringAI(request.messages());
        ChatClient chatClient = null;
        if(request.model().equals("qwen3")){
            chatClient = chatClientMap.get("ollama");
        }

        Flux<String> tokenStream = promptStream(message, chatClient);

        return messageConverter.convertMessagesToSprintAIWithStream(tokenStream, request.model());
    }

    private org.springframework.ai.chat.model.ChatResponse prompt(List<Message> message, ChatRequest chatRequest, ChatClient chatClient) {
        return chatClient.prompt()
                .messages(message)
                .call()
                .chatResponse();
    }

    private Flux<String> promptStream(List<Message> message, ChatClient chatClient) {
        return chatClient.prompt()
                .messages(message)
                .stream()
                .content();
    }

}

