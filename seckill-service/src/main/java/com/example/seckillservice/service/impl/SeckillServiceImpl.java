package com.example.seckillservice.service.impl;

import com.example.commonservice.context.UserContext;
import com.example.seckillservice.entity.SeckillActivity;
import com.example.seckillservice.entity.SeckillOrder;
import com.example.seckillservice.mapper.SeckillActivityMapper;
import com.example.seckillservice.mapper.SeckillOrderMapper;
import com.example.seckillservice.mq.message.SeckillOrderNotifyMessage;
import com.example.seckillservice.mq.sender.SeckillOrderNotifySender;
import com.example.seckillservice.service.DishService;
import com.example.seckillservice.service.SeckillService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private DishService dishService;

    @Autowired
    private SeckillOrderNotifySender seckillOrderNotifySender;

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
    public SeckillActivity findActivityById(Long activityId) {
        return seckillActivityMapper.selectById(activityId);
    }

    @Override
    public void createActivity(SeckillActivity activity) {

        dishService.deductStock(activity.getDishId(),activity.getTotalStock());

        activity.setStatus(0);
        seckillActivityMapper.insert(activity);

        loadActivityToRedis(activity.getId());
    }

    @Override
    @Transactional
    public String pay(Long seckillOrderId) {
        try {
            Long userId = UserContext.getUserId();
            SeckillOrder seckillOrder = findOrderById(seckillOrderId);
            if (seckillOrder == null) {
                return "订单不存在";
            }
            if (!seckillOrder.getUserId().equals(userId)) {
                return "无权操作此订单";
            }
            if (seckillOrder.getStatus() != 0) {
                return "订单状态不正确，无法支付";
            }

            seckillOrder.setStatus(1);
            seckillOrderMapper.updateById(seckillOrder);

            SeckillOrderNotifyMessage seckillOrderNotifyMessage = new SeckillOrderNotifyMessage();
            seckillOrderNotifyMessage.setOrderId(seckillOrder.getId());
            seckillOrderNotifyMessage.setUserId(seckillOrder.getUserId());
            seckillOrderNotifyMessage.setUsername(seckillOrder.getUsername());
            seckillOrderNotifyMessage.setSeckillPrice(seckillOrder.getSeckillPrice());
            seckillOrderNotifyMessage.setConsignee(seckillOrder.getConsignee());
            seckillOrderNotifyMessage.setPhone(seckillOrder.getPhone());
            seckillOrderNotifyMessage.setAddress(seckillOrder.getAddress());
            seckillOrderNotifyMessage.setOrderNumber(seckillOrder.getOrderNumber());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    seckillOrderNotifySender.sendNewOrderMessage(seckillOrderNotifyMessage);
                }
            });

            return "支付成功";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Override
    public SeckillOrder findOrderById(Long seckillOrderId) {
        return seckillOrderMapper.selectById(seckillOrderId);
    }


}
