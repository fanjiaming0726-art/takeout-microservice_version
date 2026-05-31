package com.example.seckillservice.mq.sender;

import com.example.seckillservice.mq.message.SeckillMessage;
import com.example.seckillservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillOrder(SeckillMessage seckillMessage){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SECKILL_EXCHANGE,
                RabbitMQConfig.SECKILL_ORDER_ROUTING_KEY,
                seckillMessage
        );
    }

}
