package com.example.fjm0313_takeout_self.service;

public interface RedisLimitService {
    boolean isAllowed(Long userId, int maxRequest, int windowSeconds);
}
