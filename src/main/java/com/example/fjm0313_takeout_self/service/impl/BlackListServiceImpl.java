package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.commonservice.redis.RedisConstant.*;

@Service
public class BlackListServiceImpl implements BlackListService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addToBlackList(Long userId, String reason) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY,userId.toString());
        redisTemplate.opsForValue().set(BLACKLIST_REASON_KEY + userId,reason);
    }

    @Override
    public void removeFromBlacklist(Long userId) {
        redisTemplate.opsForSet().remove(BLACKLIST_KEY,userId.toString());
        redisTemplate.delete(BLACKLIST_REASON_KEY + userId);
    }

    @Override
    public boolean isBlacklisted(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(BLACKLIST_KEY,userId.toString()));
    }

    @Override
    public Set<String> getAllBlacklist() {
        @SuppressWarnings("unchecked")
        Set<String> members = (Set<String>) (Set<?>)redisTemplate.opsForSet().members(BLACKLIST_KEY);
        return members;
    }
}
