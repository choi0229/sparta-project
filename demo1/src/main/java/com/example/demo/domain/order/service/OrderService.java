package com.example.demo.domain.order.service;

import com.example.demo.domain.category.repositry.CategoryRepository;
import com.example.demo.domain.order.dto.CreateOrderRequestDto;
import com.example.demo.domain.order.dto.OrderResponseDto;
import com.example.demo.domain.order.entity.Order;
import com.example.demo.domain.order.repository.OrderRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 카테고리 ID입니다: " + requestDto.getUserId()));

        Order order = Order.builder()
                .user(user)
                .totalPrice(requestDto.getTotalPrice())
                .shippingAddress(requestDto.getShippingAddress())
                .status(requestDto.getStatus())
                .build();
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.from(savedOrder);
    }
}
