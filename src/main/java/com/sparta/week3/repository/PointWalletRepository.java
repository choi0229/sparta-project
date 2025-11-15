package com.sparta.week3.repository;

import com.sparta.week3.entity.PointWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
    PointWallet findByUserId(Long userId);
}
