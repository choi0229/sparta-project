package com.sparta.project.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryRequestDto {

    @NotNull(message = "카테고리 이름은 필수입니다.")
    private String name;

    @NotNull(message = "카테고리 설명은 필수입니다.")
    private String description;

    private Long parentId;
}
