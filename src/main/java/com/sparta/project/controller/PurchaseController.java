package com.sparta.project.controller;

import com.sparta.project.common.ApiResponse;
import com.sparta.project.entity.Purchase;
import com.sparta.project.service.PurchaseService;
import com.sparta.project.service.dto.PurchaseRequestDto;
import com.sparta.project.service.dto.PurchaseResponseDto;
import com.sparta.project.service.dto.PurchaseSearchDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> CreatePurchase(@Valid @RequestBody PurchaseRequestDto request) {
        purchaseService.createPurchase(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PurchaseResponseDto>>> getSearchPurchase(@Valid @ModelAttribute  PurchaseSearchDto request, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
    Pageable pageable) {
        Page<PurchaseResponseDto> response = purchaseService.getSearchPurchase(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponseDto>> getPurchase(@PathVariable Long id) {
        PurchaseResponseDto response = purchaseService.getPurchaseProducts(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPurchase(@PathVariable Long id) {
        purchaseService.cancelPurchase(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

}
