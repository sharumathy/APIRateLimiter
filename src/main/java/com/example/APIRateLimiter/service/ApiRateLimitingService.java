package com.example.APIRateLimiter.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApiRateLimitingService {
@Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private final int MAX_REQUESTS = 10;
    private final long WINDOW = 15 * 60; // 15 minutes in seconds

    public boolean isAllowed(String userId) {
        String key = "rate:" + userId;
        Integer count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            redisTemplate.opsForValue().set(key, 1, WINDOW, TimeUnit.SECONDS);
            return true;
        }

        if (count < MAX_REQUESTS) {
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        return false;
    }
}
