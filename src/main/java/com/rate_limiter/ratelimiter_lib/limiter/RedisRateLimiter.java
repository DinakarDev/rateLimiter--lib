package com.rate_limiter.ratelimiter_lib.limiter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@Profile("redis-token")
public class RedisRateLimiter implements RateLimiter {

    private final StringRedisTemplate redisTemplate;
    private final int capacity;
    private final double refillRate;

    public RedisRateLimiter(
            StringRedisTemplate redisTemplate,
            @Value("${ratelimiter.capacity:100}") int capacity,
            @Value("${ratelimiter.refillRate:10}") double refillRate
    ) {
        this.redisTemplate = redisTemplate;
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    @Override
    public boolean allowRequest(String clientId) {
        String key = "rate:bucket:" + clientId;
        String tokensKey = key + ":tokens";
        String timestampKey = key + ":ts";

        return executeLua(tokensKey, timestampKey);
    }

    private boolean executeLua(String tokensKey, String timestampKey) {
        String luaScript = """
            local tokens_key = KEYS[1]
            local timestamp_key = KEYS[2]
            local capacity = tonumber(ARGV[1])
            local refill_rate = tonumber(ARGV[2])
            local now = tonumber(ARGV[3])

            local tokens = tonumber(redis.call("GET", tokens_key) or capacity)
            local last_refill = tonumber(redis.call("GET", timestamp_key) or now)

            local delta = math.max(0, now - last_refill)
            local refill = delta * refill_rate
            tokens = math.min(capacity, tokens + refill)

            if tokens >= 1 then
                tokens = tokens - 1
                redis.call("SET", tokens_key, tokens)
                redis.call("SET", timestamp_key, now)
                return 1
            else
                redis.call("SET", tokens_key, tokens)
                redis.call("SET", timestamp_key, now)
                return 0
            end
        """;

        Long now = System.currentTimeMillis() / 1000;

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(luaScript);
        script.setResultType(Long.class);

        List<String> keys = List.of(tokensKey, timestampKey);
        List<String> args = List.of(
            String.valueOf(capacity),
            String.valueOf(refillRate),
            String.valueOf(now)
        );

        Long result = redisTemplate.execute(script, keys, args.toArray());

        return result != null && result == 1;
    }
}
