package com.sparta.msa.project_part_3.domain.coupon.entity;

import com.sparta.msa.project_part_3.global.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Table(name = "coupons")
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String couponName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(nullable = false)
    BigDecimal discountValue;

    @Column(nullable = false)
    BigDecimal minOrderAmount;

    @Column(nullable = false)
    BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    LocalDateTime startDate;

    @Column(nullable = false)
    LocalDateTime endDate;

    @Column(nullable = false)
    Integer usageLimit;

    @Column(nullable = false)
    Integer issueCount;

    @Column(nullable = false)
    Integer usedCount;

    @Column(nullable = false)
    Boolean isDeleted;

    @Column(nullable = false, updatable = false)
    LocalDateTime createDate;

    @Column(nullable = false)
    LocalDateTime updateDate;

    @Builder
    public Coupon(
            String couponName,
            DiscountType discountType,
            BigDecimal discountValue,
            BigDecimal minOrderAmount,
            BigDecimal maxDiscountAmount,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer usageLimit
    ) {
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
    }

    public void setDelete(){
        this.isDeleted = true;
    }

    public BigDecimal calculateDiscountAmount(BigDecimal productPrice){
        if(this.discountType == DiscountType.PERCENTAGE){
            BigDecimal discount = productPrice
                    .multiply(this.discountValue)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            if(this.maxDiscountAmount != null && discount.compareTo(this.maxDiscountAmount) > 0){
                return this.maxDiscountAmount;
            }
            return discount;
        }else{
            return this.discountValue;
        }
    }

    public BigDecimal calculateDiscountRate(BigDecimal productPrice){
        if(productPrice.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount = calculateDiscountAmount(productPrice);

        return discountAmount.multiply(BigDecimal.valueOf(100)).divide(productPrice, 2, BigDecimal.ROUND_HALF_UP);
    }
}
