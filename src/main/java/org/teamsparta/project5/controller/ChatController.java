package org.teamsparta.project5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamsparta.project5.controller.dto.ChatRequest;
import org.teamsparta.project5.controller.dto.ModelListResponse;
import org.teamsparta.project5.service.ChatService;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/completions")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (request.stream()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(chatService.chatStream(request));
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(chatService.chatSync(request));
        }
    }
}
