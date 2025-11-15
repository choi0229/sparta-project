package com.sparta.week3.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    @NotNull(message = "이름은 필수입니다.")
    private String name;

    @NotNull(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;
}
