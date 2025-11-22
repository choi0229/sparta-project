package com.sparta.project4.controller;

import com.sparta.project4.controller.dto.ChatRequest;
import com.sparta.project4.controller.dto.ChatResponse;
import com.sparta.project4.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/completions")
    public ChatResponse chat(@RequestBody ChatRequest request){
        ChatResponse response;
        if(request.stream()){
            // response = chatService.chatStream(request);
            response = null;
        }else{
            response = chatService.chatSync(request);
        }

        return response;
    }
}
