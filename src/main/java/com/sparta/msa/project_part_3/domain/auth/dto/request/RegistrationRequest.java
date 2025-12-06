package com.sparta.msa.project_part_3.domain.auth.dto.request;

public record RegistrationRequest(String name, String email, String password) {
}
