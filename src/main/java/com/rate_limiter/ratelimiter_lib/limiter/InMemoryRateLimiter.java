package com.rate_limiter.ratelimiter_lib.limiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRateLimiter implements RateLimiter {
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final double refillRate;

    public InMemoryRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    @Override
    public boolean allowRequest(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(
            clientId,
            k -> new TokenBucket(capacity, refillRate)
        );
        return bucket.allowRequest();
    }
}
