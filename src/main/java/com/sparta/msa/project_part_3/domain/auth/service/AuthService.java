package com.sparta.msa.project_part_3.domain.auth.service;

import com.sparta.msa.project_part_3.domain.auth.controller.dto.LoginRequest;
import com.sparta.msa.project_part_3.domain.auth.controller.dto.LoginResponse;
import com.sparta.msa.project_part_3.domain.auth.controller.dto.RegistrationRequest;
import com.sparta.msa.project_part_3.domain.user.entity.User;
import com.sparta.msa.project_part_3.domain.user.repository.UserRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse registration(RegistrationRequest request){
        boolean existsByEmail = userRepository.existsByEmail(request.email());
        if(existsByEmail){
            throw new DomainException(DomainExceptionCode.ALREADY_USER_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .build();
        userRepository.save(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new DomainException(DomainExceptionCode.INVALID_PASSWORD);
        }

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }
}
