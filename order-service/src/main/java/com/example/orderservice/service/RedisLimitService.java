package com.example.orderservice.service;

public interface RedisLimitService {
    boolean isAllowed(Long userId, int maxRequest, int windowSeconds);
}
