package com.example.userservice.service;

public interface RedisLimitService {
    boolean isAllowed(Long userId, int maxRequest, int windowSeconds);
}
