package com.rate_limiter.ratelimiter_lib.limiter;


public class TokenBucket {
    private final int capacity;
    private final double refillRate; // tokens per second
    private double tokens;
    private long lastRefill;

    public TokenBucket(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefill = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        long now = System.nanoTime();
        double refill = ((now - lastRefill) / 1e9) * refillRate;
        tokens = Math.min(capacity, tokens + refill);
        lastRefill = now;

        if (tokens >= 1) {
            tokens--;
            return true;
        }
        return false;
    }
}
