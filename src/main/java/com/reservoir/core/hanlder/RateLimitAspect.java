package com.reservoir.core.hanlder;


import com.reservoir.core.annotation.RateLimit;
import com.reservoir.core.entity.Result;
import com.reservoir.core.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Slf4j
@Component
public class RateLimitAspect {

    private final static ConcurrentHashMap<String, Long> rateLimitMap = new ConcurrentHashMap<>();

    private final static String RATE_PRE = "rate:";

    @Pointcut("@annotation(rateLimit)")
    public void rateLimit(RateLimit rateLimit) {}

    @Around("rateLimit(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipaddr = IpUtils.getIpAddr(request);
        // String UserId = request.getHeader("userid");

        String key = RATE_PRE + ipaddr + ":" + IpUtils.getUserAgent(request).toString();
        log.info("key ------>    {}", key);
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + (rateLimit.value() * 1000L);

        if (rateLimitMap.getOrDefault(key, 0L) > currentTime) { // 过期时间大于当前时间,


            return handleRateLimitExceeded();
        }

        log.info("key: {} value: {}", key, expireTime);
        rateLimitMap.put(key, expireTime);


        try {
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            return Result.error(1014, "未知错误!", e.getMessage());
        }
    }

    private Object handleRateLimitExceeded() {
        // 返回自定义的限流响应
        return Result.error(HttpStatus.TOO_MANY_REQUESTS.value(),"Rate limit exceeded. Please try again later.");
    }
}