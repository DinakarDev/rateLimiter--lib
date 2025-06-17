# Scalable Redis-based Rate Limiter Library for

# Spring Boot

This project demonstrates a **pluggable, scalable, token-bucket-based rate limiter** built with **Spring
Boot + Redis** that can be used as a library in any microservice. It's designed to be annotation-driven
(@RateLimited) and easily switchable between **in-memory** and **Redis** backends.

## 📦 Project Structure

```
ratelimiter-lib/
├── config/
│ └── RateLimiterConfig.java # Configures backend & strategy
├── limiter/
│ ├── RateLimiter.java # Interface
│ ├── InMemoryRateLimiter.java # Local limiter
│ └── RedisRateLimiter.java # Redis-backed token bucket
├── aspect/
│ └── RateLimiterAspect.java # Applies rate limiting via AOP
├── annotation/
│ └── RateLimited.java # Custom annotation for endpoints
├── controller/
│ └── DemoController.java # Example usage
└── application.yml # Sample configuration
```
## Features

```
Token Bucket Algorithm
Pluggable backends : In-memory / Redis
Thread-safe
Annotation-based API (@RateLimited)
Easily testable and extensible
```
## ⚙ Technologies Used

```
Spring Boot
Spring AOP
Spring Data Redis
Redis (local or Docker)
Custom annotations
```
#### • • • • • • • • • •


## How it Works

### Token Bucket Logic:

```
Each client has a token bucket with a max capacity.
Tokens refill over time using a refillRate (tokens/sec).
Each incoming request consumes 1 token.
If tokens < 1 → request is rejected (HTTP 429).
```
### Redis Backend:

```
Lua script executes atomic bucket logic in Redis.
Redis keys used:
rate:bucket:{clientId}:tokens
rate:bucket:{clientId}:ts
```
### Aspect Flow:

```
Method annotated with @RateLimited triggers AOP logic.
The current user/client is extracted.
RateLimiter.allowRequest(clientId) is called.
If allowed: method proceeds; else: exception is thrown.
```
## How to Run (For Demo)

```
Start Redis :
```
```
docker run-p 6379:6379redis
```
```
Run the app
```
```
Clone the repo
```
```
Open in IntelliJ/VSCode
```
```
Run DemoController via Spring Boot run
```
```
Make test calls :
```
```
curl-X GET http://localhost:8080/api/demo-H"X-Client-Id: test-user"
```
#### • • • • • • • •

## application.yml Example

```
spring:
redis:
host: localhost
port: 6379
```
```
ratelimiter:
backend: redis
capacity: 5
refillRate: 1
```
```
spring:
profiles:
active: redis-token
```
### Profiles:

```
default: in-memory
redis-token: uses Redis backend
```
## ✍ How to Use in Other Projects

```
Package this as a JAR :
```
```
cd ratelimiter-lib
./mvnw cleaninstall
```
```
Add to your other Spring Boot app :
```
```
<dependency>
<groupId>com.yourdomain</groupId>
<artifactId>ratelimiter-lib</artifactId>
<version>1.0.0</version>
</dependency>
```
```
Use `` in your controller :
```
```
@GetMapping("/api/some-endpoint")
@RateLimited
public StringprotectedEndpoint(@RequestHeader("X-Client-Id") String
clientId) {
return "Allowed!";
}
```

## Possible Future Improvements 
```
⏱ Leaky bucket & fixed window strategies
📦 Convert to spring-boot-starter-ratelimiter
📊 Admin dashboard for stats (Redis monitoring)
🛡 Auth integration for per-user limits
```

```
"Designed and built a scalable, production-ready rate limiter library with Redis and Spring
Boot, using AOP and Lua scripting. Supports plug-and-play annotations and
configuration-driven strategies."
```


