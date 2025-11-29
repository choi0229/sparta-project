package org.teamsparta.project5.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChatRequest(
        @Schema(description = "모델", example = "ollama") String model,
        @Schema(description = "메시지 배열", example = "{ role: user, content: 안녕하세요!}")
        List<ChatMessage> messages,
        @Schema(description = "창의성", example = "0.7") Double temperature,
        @JsonProperty("max_tokens") Integer maxTokens,
        @Schema(description = "샘플링 여부", example = "false") Boolean stream
) {}
