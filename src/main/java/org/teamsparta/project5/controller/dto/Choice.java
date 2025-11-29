package org.teamsparta.project5.controller.dto;

public record Choice(
        Integer index,
        ChatMessage message,
        String finishReason
) {}
