package com.sparta.week3.service;

import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.common.annotation.Loggable;
import com.sparta.week3.controller.dto.CreateUserRequest;
import com.sparta.week3.controller.dto.UserResponse;
import com.sparta.week3.entity.PointWallet;
import com.sparta.week3.entity.User;
import com.sparta.week3.repository.PurchaseQueryRepository;
import com.sparta.week3.repository.PointWalletRepository;
import com.sparta.week3.repository.UserRepository;
import com.sparta.week3.service.dto.UserStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service

public class UserService {

    private final UserRepository userRepository;
    private final PointWalletRepository pointWalletRepository;
    private final PurchaseQueryRepository purchaseQueryRepository;

    private final Map<Long, UserStats> cache = new ConcurrentHashMap<>();
    private static final Duration TTL = Duration.ofMinutes(5); // 캐시 유지 5분

    public UserService(UserRepository userRepository, PointWalletRepository pointWalletRepository, PurchaseQueryRepository purchaseQueryRepository){
        this.userRepository = userRepository;
        this.pointWalletRepository = pointWalletRepository;
        this.purchaseQueryRepository = purchaseQueryRepository;
    }

    public UserStats getUserStats(Long id){
        UserStats cached = cache.get(id);
        if(cached != null && isExpired(cached)){
            return cached;
        }

        BigDecimal totalOrderAmount = purchaseQueryRepository.sumPurchaseAmountByUserId(id);

        UserStats stats = UserStats.builder()
                .userId(id)
                .totalOrderAmount(totalOrderAmount)
                .calculatedAt(LocalDateTime.now())
                .build();
        cache.put(id, stats);
        return stats;
    }

    private boolean isExpired(UserStats stats) {
        return stats.getCalculatedAt()
                .isBefore(LocalDateTime.now().minus(TTL));
    }

    @Loggable
    @Transactional
    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ServiceException(ServiceExceptionCode.ALREADY_EXISTS_USER);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .build();

        userRepository.save(user);

        PointWallet pointWallet= PointWallet.builder()
                .userId(user.getId())
                .balance(BigDecimal.ZERO)
                .active(true)
                .build();
        pointWalletRepository.save(pointWallet);

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Loggable
    @Transactional
    public void updateUserById(Long id, CreateUserRequest request){
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

        user.update(request.getName(), request.getEmail(), request.getPassword());
    }

    @Transactional
    public void deleteById(Long id){
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

        PointWallet pointWallet = pointWalletRepository.findByUserId(id);
        if(pointWallet == null){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
        }
        pointWallet.activeUpdate(false);
        userRepository.deleteById(id);
    }


}
