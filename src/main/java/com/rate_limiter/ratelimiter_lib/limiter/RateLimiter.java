package com.rate_limiter.ratelimiter_lib.limiter;


public interface RateLimiter {
    boolean allowRequest(String clientId);
}
