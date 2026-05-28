package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.commonservice.redis.RedisConstant.RANKING_KEY;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void increase(Long dishId, String dishName, int count) {
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY,dishId + ":" + dishName,count);
    }

    @Override
    public List<Map<String, Object>> getTopN(int n) {
        Set<ZSetOperations.TypedTuple<Object>> tuples  = redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY,0,n - 1);
        if(tuples == null){
            return new ArrayList<>();
        }

        List<Map<String ,Object>> result = new ArrayList<>();
        int rank = 1;
        for(ZSetOperations.TypedTuple<Object> tuple : tuples){
            String[] part = tuple.getValue().toString().split(":");

            Map<String,Object> map = new LinkedHashMap<>();
            map.put("rank", rank++);
            map.put("dishId",part[0]);
            map.put("dishName",part[1]);
            map.put("sales",tuple.getScore().intValue());
            result.add(map);
        }
        return result;
    }
}
