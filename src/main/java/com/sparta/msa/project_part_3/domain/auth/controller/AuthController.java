package com.sparta.msa.project_part_3.domain.auth.controller;

import com.sparta.msa.project_part_3.domain.auth.controller.dto.LoginRequest;
import com.sparta.msa.project_part_3.domain.auth.controller.dto.LoginResponse;
import com.sparta.msa.project_part_3.domain.auth.controller.dto.RegistrationRequest;
import com.sparta.msa.project_part_3.domain.auth.service.AuthService;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ApiResponse<LoginResponse> registration(@Valid @RequestBody RegistrationRequest request){
        LoginResponse response = authService.registration(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            HttpSession httpSession
            , @Valid @RequestBody LoginRequest request
    ){
        LoginResponse response = authService.login(request);

        httpSession.setAttribute("userId", response.userId());
        httpSession.setAttribute("email", response.email());

        return ApiResponse.ok(response);
    }
}
