package com.sparta.week3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.week3.common.ApiResponse;
import com.sparta.week3.controller.dto.CreateUserRequest;
import com.sparta.week3.controller.dto.UserResponse;
import com.sparta.week3.entity.PointWallet;
import com.sparta.week3.repository.PointWalletRepository;
import com.sparta.week3.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 등록 API는 요청 성공시 201을 반환한다.")
    void testCreatUser_Success() throws Exception{
        CreateUserRequest request = CreateUserRequest.builder()
                .name("홍길동")
                .email("test@example.com")
                .password("password")
                .build();

        // given : 테스트에 사용할 요청 DTO, Json body 준비
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON.toString())
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.result").value(true));


    }

    @Test
    @DisplayName("유저 삭제시 지갑 active가 false로 바뀌는지.")
    void testDeleteUser_PointWalletActive() throws Exception{
        Long requestDeleteId = 1L;
        String requestDeleteBody = objectMapper.writeValueAsString(requestDeleteId);

        mockMvc.perform(delete("/mock/users/" + requestDeleteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDeleted").value(true))
                .andExpect(jsonPath("$.walletDeactivated").value(false));


    }


}
