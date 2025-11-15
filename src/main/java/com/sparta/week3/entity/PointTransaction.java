package com.sparta.week3.entity;

import com.sparta.week3.PointTransactionType;
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
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    private PointWallet pointWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointTransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public PointTransaction(
            PointWallet pointWallet,
            Purchase purchase,
            PointTransactionType type,
            BigDecimal amount,
            LocalDateTime expiresAt
    ) {
        this.pointWallet = pointWallet;
        this.purchase = purchase;
        this.type = type;
        this.amount = amount;
        this.expiresAt = expiresAt;
    }
}
