package org.teamsparta.project5.controller.dto;

public record DocumentUploadResponse(String documentId, String filename, int chunkCount, String message) {
}
