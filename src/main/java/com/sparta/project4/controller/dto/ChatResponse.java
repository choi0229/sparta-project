package com.sparta.project4.controller.dto;

import java.util.List;

public record ChatResponse(
        String id,
        String object,
        Long created,
        String model,
        List<Choice> choices,
        Usage usage
) {}
