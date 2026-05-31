package com.example.seckillservice.mq.consumer;

import com.example.seckillservice.mq.message.SeckillOrderTimeoutMessage;
import com.example.seckillservice.config.RabbitMQConfig;
import com.example.seckillservice.entity.SeckillOrder;
import com.example.seckillservice.mapper.SeckillActivityMapper;
import com.example.seckillservice.mapper.SeckillOrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SeckillOrderTimeoutConsumer {


    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_DLX_QUEUE)
    @Transactional
    public void handleOrderTimeout(SeckillOrderTimeoutMessage message){

        Long orderId = message.getOrderId();
        System.out.println("收到超时检查消息：orderId=" + orderId + ", type=秒杀订单");
        handleSeckillOrder(orderId);


    }

    private void handleSeckillOrder(Long seckillOrderId){
        SeckillOrder seckillOrder = seckillOrderMapper.selectById(seckillOrderId);

        if(seckillOrder == null){
            return;
        }

        if(seckillOrder.getStatus() != 0){
            return;
        }

        seckillOrder.setStatus(2);
        seckillOrderMapper.updateById(seckillOrder);

        seckillActivityMapper.restoreStock(seckillOrder.getActivityId());

        System.out.println("秒杀订单超时取消成功：orderId=" + seckillOrderId);

    }


}
