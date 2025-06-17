 Scalable Redis-based Rate Limiter Library for
 Spring Boot
 This project demonstrates a pluggable, scalable, token-bucket-based rate limiter built with Spring
 Boot + Redis that can be used as a library in any microservice. It's designed to be annotation-driven
 (
 @RateLimited ) and easily switchable between in-memory and Redis backends.
 📦 Project Structure
 ratelimiter-lib/
 ├── config/
 │   └── RateLimiterConfig.java            # Configures backend & strategy
 ├── limiter/
 │   ├── RateLimiter.java                  # Interface
 │   ├── InMemoryRateLimiter.java          # Local limiter
 │   └── RedisRateLimiter.java             # Redis-backed token bucket
 ├── aspect/
 │   └── RateLimiterAspect.java            # Applies rate limiting via AOP
 ├── annotation/
 │   └── RateLimited.java                  # Custom annotation for endpoints
 ├── controller/
 │   └── DemoController.java               # Example usage
 └── application.yml                       # Sample configuration
 Features
 • 
• 
• 
• 
• 
Token Bucket Algorithm
 Pluggable backends: In-memory / Redis
 Thread-safe
 Annotation-based API (
 @RateLimited )
 Easily testable and extensible
 ⚙ Technologies Used
 • 
• 
• 
• 
• 
Spring Boot
 Spring AOP
 Spring Data Redis
 Redis (local or Docker)
 Custom annotations
 1
 How it Works
 Token Bucket Logic:
 • 
• 
Each client has a token bucket with a max 
capacity .
 Tokens refill over time using a 
• 
• 
refillRate (tokens/sec).
 Each incoming request consumes 1 token.
 If tokens < 1 → request is rejected (HTTP 429).
 Redis Backend:
 • 
• 
• 
• 
Lua script executes atomic bucket logic in Redis.
 Redis keys used:
 rate:bucket:{clientId}:tokens
 rate:bucket:{clientId}:ts
 Aspect Flow:
 1. 
2. 
3. 
4. 
Method annotated with 
@RateLimited triggers AOP logic.
 The current user/client is extracted.
 RateLimiter.allowRequest(clientId) is called.
 If allowed: method proceeds; else: exception is thrown.
 How to Run (For Demo)
 1. 
Start Redis:
 docker run-p 6379:6379 redis
 1. 
2. 
3. 
4. 
5. 
Run the app
 Clone the repo
 Open in IntelliJ/VSCode
 Run 
DemoController via Spring Boot run
 Make test calls:
 curl-X GET http://localhost:8080/api/demo-H "X-Client-Id: test-user"
 2
 application.yml Example
 spring:
 redis:
 host: localhost
 port: 6379
 ratelimiter:
 backend: redis
 capacity: 5
 refillRate: 1
 spring:
 profiles:
 active: redis-token
 Profiles:
 • 
• 
default : in-memory
 redis-token : uses Redis backend
 ✍ How to Use in Other Projects
 1. 
Package this as a JAR:
 cd ratelimiter-lib
 ./mvnw clean install
 1. 
1. 
Add to your other Spring Boot app:
 <dependency>
 <groupId>com.yourdomain</groupId>
 <artifactId>ratelimiter-lib</artifactId>
 <version>1.0.0</version>
 </dependency>
 Use `` in your controller:
 @GetMapping("/api/some-endpoint")
 @RateLimited
 public String protectedEndpoint(@RequestHeader("X-Client-Id") String
 clientId) {
 3
return "Allowed!";
 }
 1. 
Add required properties in your 
application.yml
 📈 How to Impress Recruiters
 • 
• 
• 
• 
• 
• 
 Add unit tests with JUnit + Mockito
 Add test container for Redis
 Create GitHub README with diagrams
 Write blog post on how you built it
 Deploy a working demo on Heroku or Railway
 Make it configurable via Spring Boot Starter
 Future Improvements
 • 
• 
• 
• 
⏱ Leaky bucket & fixed window strategies
 📦 Convert to 
spring-boot-starter-ratelimiter
 📊 Admin dashboard for stats (Redis monitoring)
 🛡 Auth integration for per-user limits
 Recruiter-Facing Highlights (for README/GitHub)
 "Designed and built a scalable, production-ready rate limiter library with Redis and Spring
 Boot, using AOP and Lua scripting. Supports plug-and-play annotations and
 configuration-driven strategies."
 End
 Let me know if you’d like to:
 • 
• 
• 
Add Redis test container
 Package this as a reusable starter
 Write a blog post or walkthrough
 4
