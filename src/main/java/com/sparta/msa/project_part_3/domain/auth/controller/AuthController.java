package com.sparta.msa.project_part_3.domain.auth.controller;

import com.sparta.msa.project_part_3.domain.auth.dto.request.LoginRequest;
import com.sparta.msa.project_part_3.domain.auth.dto.response.LoginResponse;
import com.sparta.msa.project_part_3.domain.auth.dto.request.RegistrationRequest;
import com.sparta.msa.project_part_3.domain.auth.service.AuthService;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
            @Valid @RequestBody LoginRequest LoginRequest
            , HttpServletRequest request
            , HttpServletResponse response
    ){
        LoginResponse loginResponse = authService.login(LoginRequest, request, response);
        return ApiResponse.ok(loginResponse);
    }

    @GetMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.ok();
    }

    @GetMapping("/status")
    public ApiResponse<LoginResponse> status() {
        LoginResponse response = authService.getLoginInfo();
        return ApiResponse.ok(response);
    }
}
