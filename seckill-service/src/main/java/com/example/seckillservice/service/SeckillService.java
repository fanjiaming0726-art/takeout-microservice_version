package com.example.seckillservice.service;

import com.example.seckillservice.entity.SeckillActivity;
import com.example.seckillservice.entity.SeckillOrder;

import java.util.List;

public interface SeckillService {
    void loadActivityToRedis(Long activityId);
    int trySeckill(Long activityId,Long userId);
    List<SeckillActivity> listActivities();
    SeckillActivity findActivityById(Long activityId);
    void createActivity(SeckillActivity activity);
    String pay(Long seckillOrderId);
    SeckillOrder findOrderById(Long seckillOrderId);
}
