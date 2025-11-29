package org.teamsparta.project5.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ModelListResponse(String object, List<ModelInfo> data) {
}
