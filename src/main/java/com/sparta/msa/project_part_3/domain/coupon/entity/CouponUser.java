package com.sparta.msa.project_part_3.domain.coupon.entity;

import com.sparta.msa.project_part_3.domain.user.entity.User;
import com.sparta.msa.project_part_3.global.enums.CouponUserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "coupon_users")
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    CouponUserStatus status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime created;

    @Column(nullable = false)
    @UpdateTimestamp
    LocalDateTime updated;

    @Builder
    public CouponUser(Coupon coupon, User user, String code, CouponUserStatus status){
        this.coupon = coupon;
        this.user = user;
        this.code = code;
        this.status = status;
    }

    public void updateStatus(CouponUserStatus status, User user){
        this.status = status;
        this.user = user;
    }
}
