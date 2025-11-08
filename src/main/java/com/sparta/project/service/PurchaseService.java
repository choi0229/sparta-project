package com.sparta.project.service;

import com.sparta.project.PurchaseStatus;
import com.sparta.project.common.ServiceException;
import com.sparta.project.common.ServiceExceptionCode;
import com.sparta.project.entity.Product;
import com.sparta.project.entity.Purchase;
import com.sparta.project.entity.PurchaseProduct;
import com.sparta.project.entity.User;
import com.sparta.project.repository.*;
import com.sparta.project.service.dto.PurchaseRequestDto;
import com.sparta.project.service.dto.PurchaseResponseDto;
import com.sparta.project.service.dto.PurchaseSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final ProductJpaRepository productJpaRepository;
    private final PurchaseJpaRepository purchaseJpaRepository;
    private final UserRepository userRepository;
    private final PurchaseProductJpaRepository purchaseProductJpaRepository;
    private final PurchaseQueryRepository purchaseQueryRepository;

    @Transactional
    public void createPurchase(PurchaseRequestDto request){
        Product product = productJpaRepository.findById(request.getProductId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

        // 상품 재고에서 주문량이 빠졌을때 0보다 적으면 예외
        if(product.getStock() < request.getQuantity()){
            throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_PRODUCT_STOCK);
        }

        // totalPrice계산
        BigDecimal quantityBd = new BigDecimal(request.getQuantity());
        BigDecimal totalPrice = product.getPrice().multiply(quantityBd);

        // 주문 정보 저장
        Purchase savedPurchase = Purchase.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .status(PurchaseStatus.PENDING)
                .totalPrice(totalPrice)
                .build();
        purchaseJpaRepository.save(savedPurchase);

        // 중간테이블 저장
        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
                .purchase(savedPurchase)
                .product(product)
                .quantity(request.getQuantity())
                .price(totalPrice)
                .build();
        purchaseProductJpaRepository.save(purchaseProduct);

        // 상품 재고 감소
        product.decreaseStock(request.getQuantity());
    }

    @Transactional
    public Page<PurchaseResponseDto> getSearchPurchase(PurchaseSearchDto request, Pageable pageable) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
        Page<Purchase> purchases = purchaseQueryRepository.getSearchPurchase(
                request.getUserId(),
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate(),
                pageable
        );
        return purchases.map(PurchaseResponseDto::from);
    }

    @Transactional
    public PurchaseResponseDto getPurchaseProducts(Long id){
        Purchase purchases = purchaseQueryRepository.getPurchaseProducts(id);
        PurchaseResponseDto response = PurchaseResponseDto.fromPurchaseProduct(purchases);
        return response;
    }

    @Transactional
    public void cancelPurchase(Long id){
        Purchase purchase = purchaseJpaRepository.findById(id)
                        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PURCHASE));
        if(!PurchaseStatus.PENDING.equals(purchase.getStatus())){
            throw new ServiceException(ServiceExceptionCode.CANCEL_NOT_ALLOWED);
        }

        List<PurchaseProduct> purchaseProduct = purchaseQueryRepository.findProductById(id);

        // 상품 재고 변경
        for(PurchaseProduct product: purchaseProduct){
            product.getProduct().increaseStock(product.getQuantity());
        }

        // 주문 상태변경
        purchase.updateStatus(PurchaseStatus.CANCELLED);
    }
}
