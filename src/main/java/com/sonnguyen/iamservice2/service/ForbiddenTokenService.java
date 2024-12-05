package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ForbiddenTokenService {
    RedisTemplate<String, Object> redisTemplate;
    JWTUtilsImpl jwtUtils;

    public void saveToken(String token) {
        try {
            Claims claims = jwtUtils.validateToken(token);
            redisTemplate.opsForValue().set(token, token, Duration.between(Instant.now(), claims.getExpiration().toInstant()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String findToken(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }
}
