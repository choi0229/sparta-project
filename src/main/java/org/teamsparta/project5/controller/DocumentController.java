package org.teamsparta.project5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.teamsparta.project5.controller.dto.DocumentUploadResponse;
import org.teamsparta.project5.entity.DocumentEntity;
import org.teamsparta.project5.service.DocumentService;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    /**
     * 파일 업로드 및 벡터화
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentUploadResponse> uploadDocument(@RequestParam("file")MultipartFile file)throws IOException {
        DocumentUploadResponse response = documentService.uploadDocument(file);
        return ResponseEntity.ok(response);
    }
}
