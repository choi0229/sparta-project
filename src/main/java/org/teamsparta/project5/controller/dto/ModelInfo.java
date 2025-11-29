package org.teamsparta.project5.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelInfo(String id, String object, long created, @JsonProperty("owned_by") String ownedBy) {
    public static ModelInfo of(String id, String ownedBy){
        return new ModelInfo(id, "model", System.currentTimeMillis() / 1000, ownedBy);
    }
}
