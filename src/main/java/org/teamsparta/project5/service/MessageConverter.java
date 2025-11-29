package org.teamsparta.project5.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.teamsparta.project5.controller.dto.ChatMessage;
import org.teamsparta.project5.controller.dto.ChatResponse;
import org.teamsparta.project5.controller.dto.Choice;
import org.teamsparta.project5.controller.dto.Usage;
import reactor.core.publisher.Flux;


import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageConverter{

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                        0, new ChatMessage("assistant", content), "stop"
                )),
                usage
        );
    }

    public Flux<ServerSentEvent<String>> convertMessagesToSpringAIWithStream(Flux<String> messages, String model){
        String id = "chatcmpl-" + UUID.randomUUID();
        long created = System.currentTimeMillis() / 1000;


        Flux<ServerSentEvent<String>> roleEvent = Flux.just(
                sseEvent(toChunkJson(id, created, model, "assistant", null, null))
        );

        Flux<ServerSentEvent<String>> contentEvents = messages.map(content ->{
            return sseEvent(toChunkJson(id, created, model, null, content, null));
        });

        Flux<ServerSentEvent<String>> stopEvent = Flux.just(
                sseEvent(toChunkJson(id, created, model, null, null, "stop"))
        );

        Flux<ServerSentEvent<String>> doneEvent = Flux.just(
                ServerSentEvent.<String>builder()
                        .data("[DONE]")
                        .build()
        );

        return roleEvent
                .concatWith(contentEvents)
                .concatWith(stopEvent)
                .concatWith(doneEvent);
    }

    private Usage extractUsage(org.springframework.ai.chat.model.ChatResponse response){
        var metadata = response.getMetadata();

        return new Usage(
                metadata.getUsage().getPromptTokens().intValue(),
                metadata.getUsage().getCompletionTokens().intValue(),
                metadata.getUsage().getTotalTokens().intValue()
        );
    }

    private String toChunkJson(String id, long created, String model, String role, String content, String finishReason){
        try{
            Map<String, Object> chunk = new LinkedHashMap<>();
            chunk.put("id", id);
            chunk.put("object", "chat.completion.chunk");
            chunk.put("created", created);
            chunk.put("model", model);

            Map<String, Object> delta = new HashMap<>();
            if(role != null){
                delta.put("role", role);
            }
            if(content != null){
                delta.put("content", content);
            }

            Map<String, Object> choice = new HashMap<>();
            choice.put("index", 0);
            choice.put("delta", delta);
            choice.put("finish_reason", finishReason);

            chunk.put("choices", List.of(choice));

            return objectMapper.writeValueAsString(chunk);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    private ServerSentEvent<String> sseEvent(String json) {
        return ServerSentEvent.<String>builder()
                .data(json)
                .build();
    }
}
