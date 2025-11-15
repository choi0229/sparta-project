package com.sparta.week3.service;

import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.controller.dto.CreateUserRequest;
import com.sparta.week3.entity.User;
import com.sparta.week3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .name("테스트")
                .email("test@example.com")
                .passwordHash("password")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists(){
        CreateUserRequest request = CreateUserRequest.builder()
                .name("홍길동")
                .email("test@example.com")
                .password("1234")
                .build();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining(ServiceExceptionCode.ALREADY_EXISTS_USER.getMessage());
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, org.mockito.Mockito.never()).save(any(User.class));

    }
}
