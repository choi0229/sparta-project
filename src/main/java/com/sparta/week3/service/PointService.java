package com.sparta.week3.service;

import com.sparta.week3.PointTransactionType;
import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.common.annotation.Loggable;
import com.sparta.week3.entity.PointTransaction;
import com.sparta.week3.entity.PointWallet;
import com.sparta.week3.entity.Purchase;
import com.sparta.week3.repository.PointTransactionQueryRepository;
import com.sparta.week3.repository.PointTransactionRepository;
import com.sparta.week3.repository.PointWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PointService {
    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;

    private final BigDecimal MIN_USE_POINT = BigDecimal.valueOf(100);
    private final BigDecimal MAX_USE_POINT = BigDecimal.valueOf(10_000);
    private final PointTransactionQueryRepository pointTransactionQueryRepository;

    public PointService(PointWalletRepository pointWalletRepository, PointTransactionRepository pointTransactionRepository, PointTransactionQueryRepository pointTransactionQueryRepository) {
        this.pointWalletRepository = pointWalletRepository;
        this.pointTransactionRepository = pointTransactionRepository;
        this.pointTransactionQueryRepository = pointTransactionQueryRepository;
    }

    @Loggable
    @Transactional
    public void getPoint(Long userId, Purchase purchase) {
        PointWallet pointwallet = pointWalletRepository.findByUserWithPessimisticLock(userId);

        if(!pointwallet.getActive()){
            throw new ServiceException(ServiceExceptionCode.POINT_WALLET_NOT_ALLOWED);
        }

        LocalDateTime expiresDate = LocalDateTime.now().plusYears(1);

        pointwallet.increaseBalance(purchase.getEarnedPoint());
        PointTransaction transaction = PointTransaction.builder()
                .pointWallet(pointwallet)
                .purchase(purchase)
                .type(PointTransactionType.EARN)
                .amount(purchase.getEarnedPoint())
                .expiresAt(expiresDate)
                .build();
        pointTransactionRepository.save(transaction);
    }

    @Loggable
    @Transactional
    public void usedPoint(Long userId, Purchase purchase) {
        PointWallet pointwallet = pointWalletRepository.findByUserWithPessimisticLock(userId);

        if(!pointwallet.getActive()){
            throw new ServiceException(ServiceExceptionCode.POINT_WALLET_NOT_ALLOWED);
        }

        if(purchase.getUsedPoints().compareTo(pointwallet.getBalance()) == 1){
            throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_POINT_BALANCE);
        }

        if(purchase.getUsedPoints().compareTo(MIN_USE_POINT) < 0 || purchase.getUsedPoints().compareTo(MAX_USE_POINT) > 0){
            throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_POINT_BALANCE);
        }

        pointwallet.decreaseBalance(purchase.getUsedPoints());
        PointTransaction transaction = PointTransaction.builder()
                .pointWallet(pointwallet)
                .purchase(purchase)
                .type(PointTransactionType.DEDUCTED)
                .amount(purchase.getUsedPoints())
                .expiresAt(null)
                .build();
        pointTransactionRepository.save(transaction);
    }

    @Transactional
    public void expirePoints(){
        List<PointTransaction> pointTransactions = pointTransactionQueryRepository.findExpiredTransactions(LocalDateTime.now());
        for(PointTransaction pointTransaction : pointTransactions){
            Long userId = pointTransaction.getPointWallet().getUserId();
            BigDecimal expireAmount = pointTransaction.getAmount();

            PointWallet pointWallet = pointWalletRepository.findByUserWithPessimisticLock(userId);
            pointWallet.decreaseBalance(expireAmount);

            PointTransaction expire = PointTransaction.builder()
                    .pointWallet(pointWallet)
                    .purchase(pointTransaction.getPurchase())
                    .type(PointTransactionType.EXTINCTION)
                    .amount(expireAmount)
                    .expiresAt(null)
                    .build();
            pointTransactionRepository.save(expire);
        }
    }
}
