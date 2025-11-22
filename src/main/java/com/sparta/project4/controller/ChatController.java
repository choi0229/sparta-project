package com.sparta.project4.controller;

import com.sparta.project4.controller.dto.ChatRequest;
import com.sparta.project4.controller.dto.ChatResponse;
import com.sparta.project4.controller.dto.ModelInfo;
import com.sparta.project4.controller.dto.ModelListResponse;
import com.sparta.project4.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/completions")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request){
        if(request.stream()){
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(chatService.chatStream(request));
        }else{
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(chatService.chatSync(request));
        }
    }

    @GetMapping
    public ModelListResponse getModels(){
        ModelListResponse response = chatService.getModels();
        return response;
    }
}
