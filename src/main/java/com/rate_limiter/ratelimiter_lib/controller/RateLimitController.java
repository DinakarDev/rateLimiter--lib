package com.rate_limiter.ratelimiter_lib.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rate_limiter.ratelimiter_lib.annotation.RateLimited;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    @RateLimited(key = "ip")
    @GetMapping("/data")
    public String getData() {
        return "Success! ✅";
    }
}