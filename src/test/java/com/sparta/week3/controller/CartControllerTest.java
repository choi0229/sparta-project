package com.sparta.week3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.week3.CategoryGrade;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.controller.dto.AddCartItemRequest;
import com.sparta.week3.entity.Product;
import com.sparta.week3.entity.User;
import com.sparta.week3.repository.ProductRepository;
import com.sparta.week3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp(){
        // 테스트 사용자
        user = User.builder()
                .name("테스터")
                .email("test@example.com")
                .passwordHash("1234")
                .build();
        user = userRepository.save(user);

        product = Product.builder()
                .name("노트북")
                .price(BigDecimal.valueOf(1_000_000))
                .stock(20)
                .category(CategoryGrade.NORMAL)
                .build();
        product = productRepository.save(product);
    }

    @Test
    @DisplayName("장바구나 추가 API 성공")
    void addItem_Success() throws Exception{
        AddCartItemRequest request = AddCartItemRequest.builder()
                .productId(product.getId())
                .quantity(2)
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/carts/items/"+user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @DisplayName("최대 품목 수 초과시 400 에러")
    void addItem_ExceedMaxItems_Returns() throws Exception{
        for (int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .name("상품" + i)
                    .price(BigDecimal.valueOf(10000))
                    .stock(100)
                    .category(CategoryGrade.NORMAL)
                    .build();
            productRepository.save(product);

            AddCartItemRequest request = AddCartItemRequest.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build();

            mockMvc.perform(post("/api/carts/items/" + user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        // 11번째 시도
        AddCartItemRequest request = AddCartItemRequest.builder()
                .productId(product.getId())
                .quantity(1)
                .build();
        String requestBody = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/carts/items/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(ServiceExceptionCode.CART_MAX_ITEMS_EXCEEDED.getMessage()));

    }

    @Test
    @DisplayName("장바구니 요약 테스트")
    void getItemSummary_Success() throws Exception{
        for (int i = 0; i < 6; i++) {
            Product product = Product.builder()
                    .name("상품" + i)
                    .price(BigDecimal.valueOf(10000))
                    .stock(100)
                    .category(CategoryGrade.NORMAL)
                    .build();
            productRepository.save(product);

            AddCartItemRequest request = AddCartItemRequest.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build();

            mockMvc.perform(post("/api/carts/items/" + user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        mockMvc.perform(get("/api/carts/items/summary/"+user.getId()+"/WELCOME10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));;
    }
}
