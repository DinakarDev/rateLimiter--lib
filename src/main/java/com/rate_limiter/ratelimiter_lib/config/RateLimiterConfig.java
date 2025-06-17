package com.rate_limiter.ratelimiter_lib.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.rate_limiter.ratelimiter_lib.limiter.InMemoryRateLimiter;
import com.rate_limiter.ratelimiter_lib.limiter.RateLimiter;
import com.rate_limiter.ratelimiter_lib.limiter.RedisRateLimiter;

@Configuration
public class RateLimiterConfig {

    private final StringRedisTemplate redisTemplate;

    public RateLimiterConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${ratelimiter.strategy:token-bucket}")
    private String strategy;

    @Value("${ratelimiter.backend:in-memory}")
    private String backend;

    @Value("${ratelimiter.capacity:10}")
    private int capacity;

    @Value("${ratelimiter.refillRate:5}")
    private double refillRate;

    @Bean
    public RateLimiter rateLimiter() {
        if (backend.equalsIgnoreCase("in-memory") && strategy.equalsIgnoreCase("token-bucket")) {
            return new InMemoryRateLimiter(capacity, refillRate);
        } else if (backend.equalsIgnoreCase("redis") && strategy.equalsIgnoreCase("token-bucket")) {
            return new RedisRateLimiter(redisTemplate, capacity, refillRate); // ✅ Use your existing class
        }

        throw new IllegalArgumentException("Unsupported backend/strategy: " + backend + "/" + strategy);
    }
}
