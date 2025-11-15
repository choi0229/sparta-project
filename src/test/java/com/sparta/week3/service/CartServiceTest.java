package com.sparta.week3.service;

import com.sparta.week3.CategoryGrade;
import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.entity.Product;
import com.sparta.week3.entity.User;
import com.sparta.week3.repository.ProductRepository;
import com.sparta.week3.repository.UserRepository;
import com.sparta.week3.service.dto.CartItemDto;
import com.sparta.week3.service.dto.CartSummaryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @InjectMocks
    CartService cartService;

    @Mock
    InMemoryCartStorage inMemoryCartStorage;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    private User user;
    private Product product;
    private Product product2;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .name("테스트")
                .email("test@example.com")
                .passwordHash("password")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        product = Product.builder()
                .name("노트북")
                .price(BigDecimal.valueOf(7_000))
                .stock(100)
                .category(CategoryGrade.NORMAL)
                .build();
        ReflectionTestUtils.setField(product, "id", 1L);

        product2 = Product.builder()
                .name("아이폰")
                .price(BigDecimal.valueOf(10_000))
                .stock(90)
                .category(CategoryGrade.PREMIUM)
                .build();
        ReflectionTestUtils.setField(product2, "id", 2L);
    }

    @Test
    void getItemSummary_shouldSuccess(){
        Long id = 1L;
        String couponCode = "WELCOME10";

        CartItemDto cartItem1 = CartItemDto.builder()
                .productId(1L)
                .productName("노트북")
                .price(BigDecimal.valueOf(7_000))
                .quantity(2)
                .category(CategoryGrade.NORMAL)
                .build();

        CartItemDto cartItem2 = CartItemDto.builder()
                .productId(2L)
                .productName("아이폰")
                .price(BigDecimal.valueOf(10_000))
                .quantity(3)
                .category(CategoryGrade.PREMIUM)
                .build();

        when(userRepository.existsById(id)).thenReturn(true);
        when(inMemoryCartStorage.getCartItems(id)).thenReturn(List.of(cartItem1, cartItem2)); // 가짜 장바구니에 이런게 있다고 가정

        // act
        CartSummaryDto response = cartService.getCartSummary(id, couponCode);


        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getTotalItemCount()).isEqualTo(2);
        assertThat(response.getTotalAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(44_000));

        BigDecimal expectedPoints =
                BigDecimal.valueOf(7_000).multiply(BigDecimal.valueOf(2)).multiply(CategoryGrade.NORMAL.getPointWeight())
                        .add(
                                BigDecimal.valueOf(10_000).multiply(BigDecimal.valueOf(3)).multiply(CategoryGrade.PREMIUM.getPointWeight())
                        )
                        .multiply(BigDecimal.valueOf(1.1))
                        .setScale(0, RoundingMode.DOWN);
        assertThat(response.getExpectedPoints())
                .isEqualByComparingTo(expectedPoints);

        CartItemDto cartItem = CartItemDto.builder()
                        .productId(1L)
                        .productName("노트북")
                        .price(BigDecimal.valueOf(7_000))
                        .quantity(2)
                        .category(CategoryGrade.NORMAL)
                        .build();
    }
}
