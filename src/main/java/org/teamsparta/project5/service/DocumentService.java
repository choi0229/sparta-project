package org.teamsparta.project5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.teamsparta.project5.controller.dto.DocumentUploadResponse;
import org.teamsparta.project5.entity.DocumentEntity;
import org.teamsparta.project5.repository.DocumentRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final VectorStore vectorStore;
    private final DocumentRepository documentRepository;

    /**
     * 문서 업로드 및 벡터화
     */
    @Transactional
    public DocumentUploadResponse uploadDocument(MultipartFile file) throws IOException {
        // String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String content;
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if(contentType.equals("application/pdf") || filename.toLowerCase().endsWith(".pdf")) {
            content = extractTextFromPDF(file);
        }else if(contentType.equals("application/msword") || filename.toLowerCase().endsWith(".docx") || filename.toLowerCase().endsWith(".doc")){
            content = extractTextFromDOCX(file);
        }else{
            content = new String(file.getBytes(), StandardCharsets.UTF_8)
                    .replace("\u0000", "")
                    .trim();
        }
        DocumentEntity documentEntity = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .filename(file.getOriginalFilename())
                .content(content)
                .contentType(file.getContentType())
                .build();

        List<Document> chunks = splitDocument(content, documentEntity.getId().toString(), file.getOriginalFilename());
        documentEntity.setChunkCount(chunks.size());
        documentRepository.save(documentEntity);

        vectorStore.add(chunks);

        DocumentUploadResponse response = new DocumentUploadResponse(
                documentEntity.getId().toString(),
                documentEntity.getFilename(),
                documentEntity.getChunkCount(),
                "문서가 성공적으로 업로드되었습니다."
        );

        return response;
    }

    private List<Document> splitDocument(String content, String documentId, String filename) {
        TextSplitter splitter = new TokenTextSplitter(
                1000,
                100,
                5,
                Integer.MAX_VALUE,
                true
        );

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("document_id", documentId);
        metadata.put("filename", filename);
        metadata.put("source", "user_upload");

        Document document = new Document(content, metadata);
        List<Document> originalChunks = splitter.split(document);

        return originalChunks.stream()
                .map(chunk -> {
                    Map<String, Object> newMetadata = new HashMap<>(chunk.getMetadata());
                    return new Document(UUID.randomUUID().toString(),chunk.getText(), newMetadata);
                })
                .toList();

    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {  // ⬅️ 변경
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // NULL 바이트 및 제어 문자 제거
            return text.replace("\u0000", "")
                    .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                    .trim();
        }
    }

    private String extractTextFromDOCX(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream());
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            String text = extractor.getText();

            return text.replace("\u0000", "")
                    .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                    .trim();
        }
    }

    /**
     * 텍스트로 직접 문서 추가
     */
    @Transactional
    public DocumentEntity addTextDocument(String filename, String content) {
        log.info("텍스트 문서 추가: {}", filename);

        DocumentEntity documentEntity = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .filename(filename)
                .content(content)
                .contentType("text/plain")
                .build();

        List<Document> chunks = splitDocument(content, documentEntity.getId().toString(), filename);
        documentEntity.setChunkCount(chunks.size());
        documentRepository.save(documentEntity);

        vectorStore.add(chunks);

        return documentEntity;
    }
}
