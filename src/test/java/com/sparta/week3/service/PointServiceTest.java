package com.sparta.week3.service;

import com.sparta.week3.PointTransactionType;
import com.sparta.week3.PurchaseStatus;
import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.entity.PointTransaction;
import com.sparta.week3.entity.PointWallet;
import com.sparta.week3.entity.Purchase;
import com.sparta.week3.entity.User;
import com.sparta.week3.repository.PointTransactionQueryRepository;
import com.sparta.week3.repository.PointTransactionRepository;
import com.sparta.week3.repository.PointWalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @InjectMocks
    PointService pointService;

    @Mock
    PointWalletRepository pointWalletRepository;

    @Mock
    PointTransactionQueryRepository pointTransactionQueryRepository;

    @Mock
    PointTransactionRepository pointTransactionRepository;

    private PointWallet activePointWallet;
    private PointTransaction pointTransaction;
    private Purchase purchase;
    private Purchase purchase2;
    private Purchase purchase3;
    private User user;
    private PointTransaction expiredEarn;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .name("테스트")
                .email("test@example.com")
                .passwordHash("password")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        purchase = Purchase.builder()
                .user(user)
                .earnedPoint(BigDecimal.valueOf(500))
                .usedPoints(BigDecimal.valueOf(0))
                .status(PurchaseStatus.APPROVED)
                .shippingAddress("서울시 강남구")
                .build();
        ReflectionTestUtils.setField(purchase, "id", 1L);

        purchase2 = Purchase.builder()
                .user(user)
                .earnedPoint(BigDecimal.valueOf(1000))
                .usedPoints(BigDecimal.valueOf(300))
                .status(PurchaseStatus.APPROVED)
                .shippingAddress("서울시 강남구")
                .build();
        ReflectionTestUtils.setField(purchase2, "id", 2L);

        purchase3 = Purchase.builder()
                .user(user)
                .earnedPoint(BigDecimal.valueOf(1000))
                .usedPoints(BigDecimal.valueOf(50))
                .status(PurchaseStatus.APPROVED)
                .shippingAddress("서울시 강남구")
                .build();
        ReflectionTestUtils.setField(purchase3, "id", 3L);

        activePointWallet = PointWallet.builder()
                .userId(1L)
                .active(true)
                .balance(BigDecimal.valueOf(10_000))
                .build();
        ReflectionTestUtils.setField(activePointWallet, "id", 1L);

        expiredEarn = PointTransaction.builder()
                .pointWallet(activePointWallet)
                .purchase(purchase)
                .type(PointTransactionType.EARN)
                .amount(BigDecimal.valueOf(3_000))
                .expiresAt(LocalDateTime.now().minusDays(1)) // 어제 만료
                .build();
        ReflectionTestUtils.setField(expiredEarn, "id", 100L);
    }

    @Test
    void getPoint_shouldSucceed(){
        Long id = 1L;
        BigDecimal expectedEarnedPoint = purchase.getEarnedPoint();

        when(pointWalletRepository.findByUserWithPessimisticLock(id))
                .thenReturn(activePointWallet);

        pointService.getPoint(id, purchase);
        assertThat(activePointWallet.getBalance())
                .isEqualByComparingTo(expectedEarnedPoint);

        verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
        verify(pointWalletRepository, times(1)).findByUserWithPessimisticLock(id);
    }

    @Test
    void usedPoint_shouldThrowException_whenMinPoint(){
        Long id = 1L;

        when(pointWalletRepository.findByUserWithPessimisticLock(id))
                .thenReturn(activePointWallet);

        assertThatThrownBy(() -> pointService.usedPoint(id, purchase3))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining(ServiceExceptionCode.INSUFFICIENT_POINT_BALANCE.getMessage());
    }

    @Test
    void usedPoint_shouldThrowException_insufficientPoint(){
        Long id = 1L;

        when(pointWalletRepository.findByUserWithPessimisticLock(id))
                .thenReturn(activePointWallet);

        ;
        assertThatThrownBy(() -> pointService.usedPoint(id, purchase2))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining(ServiceExceptionCode.INSUFFICIENT_POINT_BALANCE.getMessage());
    }

    @Test
    void expirePoints_shouldDecreaseBalance_andSaveExtinctionTransaction() {
        Long userId = activePointWallet.getUserId();

        when(pointTransactionQueryRepository.findExpiredTransactions(any(LocalDateTime.class)))
                .thenReturn(List.of(expiredEarn));
        when(pointWalletRepository.findByUserWithPessimisticLock(userId))
                .thenReturn(activePointWallet);

        pointService.expirePoints();
        assertThat(activePointWallet.getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(7_000));
        ArgumentCaptor<PointTransaction> captor = ArgumentCaptor.forClass(PointTransaction.class);
        verify(pointTransactionRepository, times(1)).save(captor.capture());

        PointTransaction saved = captor.getValue();
        assertThat(saved.getType()).isEqualTo(PointTransactionType.EXTINCTION);
        assertThat(saved.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(3_000));
        assertThat(saved.getPointWallet()).isEqualTo(activePointWallet);
        assertThat(saved.getPurchase()).isEqualTo(purchase);

        verify(pointTransactionQueryRepository, times(1))
                .findExpiredTransactions(any(LocalDateTime.class));
        verify(pointWalletRepository, times(1))
                .findByUserWithPessimisticLock(userId);
    }

}
