package com.rate_limiter.ratelimiter_lib.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    String key() default ""; // optional client key
}