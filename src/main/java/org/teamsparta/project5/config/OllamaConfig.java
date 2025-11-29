package org.teamsparta.project5.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.ollama")
@Slf4j
@Setter
public class OllamaConfig {

    private String baseUrl;

    @Bean
    public OllamaApi ollamaApi() {
        return OllamaApi.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean(name = "ollamaChatModel")
    public OllamaChatModel ollamaChatModel(OllamaApi ollamaApi) {
        OllamaChatOptions options = OllamaChatOptions.builder()
                .model("qwen2.5:3b")
                .temperature(0.7)
                .numPredict(500)
                .build();

        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

    @Bean(name = "ollamaChatClient")
    public ChatClient ollamaChatClient(@Qualifier("ollamaChatModel") OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        당신은 친절하고 전문적인 AI 어시스턴트입니다.
                        
                        다음 원칙을 따라 응답해주세요:
                        1. 정확하고 사실에 기반한 정보를 제공합니다
                        2. 명확하고 이해하기 쉬운 언어를 사용합니다
                        3. 필요한 경우 단계별로 설명합니다
                        4. 불확실한 정보는 명시적으로 표시합니다
                        5. 사용자의 질문 의도를 정확히 파악하여 답변합니다
                        
                        한국어로 답변하며, 존댓말을 사용합니다.
                        """)
                .build();
    }
}
