package com.sparta.project4.service;

import com.sparta.project4.controller.dto.ChatMessage;
import com.sparta.project4.controller.dto.ChatResponse;
import com.sparta.project4.controller.dto.Choice;
import com.sparta.project4.controller.dto.Usage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class MessageConverter{

    public List<Message> convertMessagesToSpringAI(List<ChatMessage> messages){
        return messages.stream()
                .map(m -> (Message) switch (m.role()) {
                    case "system" -> new SystemMessage(m.content());
                    case "user" -> new UserMessage(m.content());
                    case "assistant" -> new AssistantMessage(m.content());
                    default -> throw new IllegalArgumentException("Unknown role: " + m.role());
                })
                .toList();
    }

    public ChatResponse convertMessageFromSpringAI(org.springframework.ai.chat.model.ChatResponse response, String model){
        String content = response.getResult().getOutput().getText();

        Usage usage = extractUsage(response);

        return new ChatResponse(
                "chatcmpl-"+ UUID.randomUUID().toString(),
                "chat.completion",
                System.currentTimeMillis() / 1000,
                model,
                List.of(new Choice(
                        0, new ChatMessage("assisitant", content), "stop"
                )),
                usage
        );
    }

    private Usage extractUsage(org.springframework.ai.chat.model.ChatResponse response){
        var metadata = response.getMetadata();

        return new Usage(
                metadata.getUsage().getPromptTokens().intValue(),
                metadata.getUsage().getCompletionTokens().intValue(),
                metadata.getUsage().getTotalTokens().intValue()
        );
    }
}
