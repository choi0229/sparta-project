package com.sparta.msa.project_part_3.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(Long userId, String email) {
}
