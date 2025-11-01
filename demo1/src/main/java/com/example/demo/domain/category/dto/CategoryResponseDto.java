package com.example.demo.domain.category.dto;

import com.example.demo.domain.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String parentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryResponseDto from(Category category){
        return builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentName(category.getParent() != null ?
                        category.getParent().getName() : null)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
