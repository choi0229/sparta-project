package org.teamsparta.project5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamsparta.project5.controller.dto.ModelListResponse;
import org.teamsparta.project5.service.ChatService;

@RestController
@RequestMapping("/v1/models")
@RequiredArgsConstructor
public class ModelController {

    private final ChatService chatService;

    @GetMapping
    public ModelListResponse getModels() {
        return chatService.getModels();
    }
}
