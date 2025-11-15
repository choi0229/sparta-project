package com.sparta.week3.entity;

import com.sparta.week3.PurchaseStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private BigDecimal earnedPoint;

    @Column(nullable = true)
    private BigDecimal usedPoints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Purchase(
            User user,
            BigDecimal totalPrice,
            BigDecimal earnedPoint,
            BigDecimal usedPoints,
            PurchaseStatus status,
            String shippingAddress
    ){
        this.user = user;
        this.totalPrice = totalPrice;
        this.earnedPoint = earnedPoint;
        this.usedPoints = usedPoints;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
}
