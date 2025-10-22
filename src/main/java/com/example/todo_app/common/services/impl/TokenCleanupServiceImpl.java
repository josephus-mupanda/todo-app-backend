package com.example.todo_app.common.services.impl;

import com.example.todo_app.common.repositories.ConfirmationTokenRepository;
import com.example.todo_app.common.repositories.PasswordResetTokenRepository;
import com.example.todo_app.common.services.TokenCleanupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanupServiceImpl implements TokenCleanupService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(fixedRate = 86400000)  // Run once a day
    public void cleanUpExpiredTokens() {
        cleanUpExpiredConfirmationTokens();
        cleanUpExpiredPasswordResetTokens();
    }

    private void cleanUpExpiredConfirmationTokens() {
        confirmationTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    private void cleanUpExpiredPasswordResetTokens() {
       passwordResetTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}

//@Service
//public class TokenBlacklistServiceImpl implements TokenBlacklistService {

//    private final RedisTemplate<String, String> redisTemplate;
//
//    public TokenBlacklistServiceImpl(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public void blacklistToken(String token, long expirationMillis) {
//        // Store token in Redis with a TTL equal to remaining lifetime
//        redisTemplate.opsForValue().set(token, "BLACKLISTED", expirationMillis, TimeUnit.MILLISECONDS);
//    }
//
//    @Override
//    public boolean isTokenBlacklisted(String token) {
//        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
//    }
//
//    @Override
//    public void removeToken(String token) {
//        redisTemplate.delete(token);
//    }
//}

