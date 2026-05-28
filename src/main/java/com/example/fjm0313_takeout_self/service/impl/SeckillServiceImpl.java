package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.entity.SeckillActivity;
import com.example.fjm0313_takeout_self.mapper.SeckillActivityMapper;
import com.example.fjm0313_takeout_self.service.DishService;
import com.example.fjm0313_takeout_self.service.SeckillService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;


import static com.example.commonservice.redis.RedisConstant.SECKILL_STOCK_KEY;
import static com.example.commonservice.redis.RedisConstant.SECKILL_USERS_KEY;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private DishService dishService;

    private DefaultRedisScript<Long> seckillScript;



    @PostConstruct
    public void init(){
        seckillScript = new DefaultRedisScript<>();

        seckillScript.setLocation(new ClassPathResource("seckill.lua"));

        seckillScript.setResultType(Long.class);
    }

    @Override
    public void loadActivityToRedis(Long activityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if(activity == null){
            throw new RuntimeException("秒杀活动不存在");
        }
        String stockKey = SECKILL_STOCK_KEY + activityId;
        redisTemplate.opsForValue().set(stockKey,activity.getTotalStock());

    }

    @Override
    public int trySeckill(Long activityId, Long userId) {
        String stockKey = SECKILL_STOCK_KEY + activityId;
        String usersKey = SECKILL_USERS_KEY + userId;

        Long result = redisTemplate.execute(
                seckillScript,
                Arrays.asList(stockKey,usersKey),
                userId.toString()
        );

        return result == null ? -3 : result.intValue();
    }

    @Override
    public List<SeckillActivity> listActivities() {
        return seckillActivityMapper.selectList(null);
    }

    @Override
    public SeckillActivity findById(Long activityId) {
        return seckillActivityMapper.selectById(activityId);
    }

    @Override
    public void createActivity(SeckillActivity activity) {

        dishService.deductStock(activity.getDishId(),activity.getTotalStock());

        activity.setStatus(0);
        seckillActivityMapper.insert(activity);

        loadActivityToRedis(activity.getId());
    }


}
