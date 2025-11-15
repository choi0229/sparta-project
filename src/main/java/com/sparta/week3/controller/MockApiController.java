package com.sparta.week3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/mock")
public class MockApiController {

    private final Map<Long, MockUser> users = new ConcurrentHashMap<>();
    private final Map<Long, MockPointWallet> pointWallets = new ConcurrentHashMap<>();

    MockApiController() {
        users.put(1L, new MockUser(1L,"홍길동", "123@example.com", "1234"));
        pointWallets.put(1L, new MockPointWallet(1L,1L, BigDecimal.valueOf(10_000), true));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        MockUser user = users.get(id);
        MockPointWallet pointWallet = pointWallets.get(id);
        user.markAsDeleted();

        if(pointWallet != null) {
            pointWallet.setActive(false);
        }
        MockUserDeleteResponse response = new MockUserDeleteResponse(id, user.deleted, pointWallet.isActive());
        return ResponseEntity.ok(response);
    }

    public record MockUserDeleteResponse(
            long userId,
            boolean userDeleted,
            boolean walletDeactivated
    ) {
    }

    private static class MockUser {
        private final long id;
        private final String name;
        private final String email;
        private String password;
        private boolean deleted;

        private MockUser(long id, String name, String email, String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.deleted = false;
        }

        public void markAsDeleted() {
            this.deleted = true;
        }
    }

    private static class MockPointWallet {
        private final long id;
        private final Long userId;
        private final BigDecimal balance;
        private boolean active;

        private MockPointWallet(long id, Long userId, BigDecimal balance, boolean active) {
            this.id = id;
            this.userId = userId;
            this.balance = balance;
            this.active = active;
        }
        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }
    }
}
