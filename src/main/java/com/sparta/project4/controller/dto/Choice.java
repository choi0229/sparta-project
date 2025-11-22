package com.sparta.project4.controller.dto;

public record Choice(
        Integer index,
        ChatMessage message,
        String finishReason
) {
}
