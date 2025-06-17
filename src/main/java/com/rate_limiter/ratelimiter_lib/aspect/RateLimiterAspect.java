package com.rate_limiter.ratelimiter_lib.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import com.rate_limiter.ratelimiter_lib.annotation.RateLimited;
import com.rate_limiter.ratelimiter_lib.limiter.RateLimiter;

@Aspect
@Component
public class RateLimiterAspect {

    private final RateLimiter rateLimiter;

    public RateLimiterAspect(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Around("@annotation(rateLimited)")
    public Object checkRateLimit(ProceedingJoinPoint pjp, RateLimited rateLimited) throws Throwable {
        // Use IP or username or key
        String clientId = getClientId();
        if (rateLimiter.allowRequest(clientId)) {
            return pjp.proceed();
        }
        throw new RuntimeException("Too many requests");
    }

    private String getClientId() {
        // For now return dummy — later read from JWT, Header, or IP
        return "user-1";
    }
}
