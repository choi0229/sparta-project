package org.teamsparta.project5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamsparta.project5.controller.dto.AnswerResponse;
import org.teamsparta.project5.controller.dto.QuestionRequest;
import org.teamsparta.project5.service.RagService;

@Slf4j
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/ask")
    public ResponseEntity<AnswerResponse> ask(@RequestBody QuestionRequest request){
        try{
            log.info("질문 요청 : {}", request.question());

            String answer = ragService.ask(request.question());

            return ResponseEntity.ok(new AnswerResponse(answer, "success"));
        }catch(Exception e){
            log.error("질문 실패 : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AnswerResponse("질문 처리중 오류 발생: "+ e.getMessage(), "error"));
        }
    }
}
