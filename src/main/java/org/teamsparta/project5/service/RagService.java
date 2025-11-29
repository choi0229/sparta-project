package org.teamsparta.project5.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RagService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;


    public RagService(@Qualifier("anthropicChatClient")ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(10)
                        .build()
        );

        if(relevantDocs.isEmpty()) {
            return "죄송하지만 관련 정보를 찾을 수 없습니다.";
        }

        relevantDocs.forEach(doc -> {
            log.debug("문서 발경 - ID: {}, 메타데이터: {}", doc.getId(), doc.getMetadata());
        });

        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String prompt = String.format("""
                다음 문서들을 참고하여 질문에 답변해주세요.
                문서에 없는 내용은 답변하지 마세요.
                답변은 한국어로 작성해주세요.
                
                [참고 문서]
                %s
                
                [질문]
                %s
                
                [답변]
                """, context, question);

        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return answer;
    }
}
