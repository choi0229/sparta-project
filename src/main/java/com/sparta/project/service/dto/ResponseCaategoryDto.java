package com.sparta.project.service.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.project.entity.Category;
import com.sparta.project.entity.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResponseCaategoryDto {

    private Long id;


    private String name;

    private Category parent;

    private List<Category> children = new ArrayList<>();

    private List<Product> products = new ArrayList<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
