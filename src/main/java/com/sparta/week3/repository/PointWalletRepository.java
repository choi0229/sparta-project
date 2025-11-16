package com.sparta.week3.repository;

import com.sparta.week3.entity.PointWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
    PointWallet findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pw FROM PointWallet pw WHERE pw.userId = :userId")
    PointWallet findByUserWithPessimisticLock(@Param("userId") Long userId);
}
