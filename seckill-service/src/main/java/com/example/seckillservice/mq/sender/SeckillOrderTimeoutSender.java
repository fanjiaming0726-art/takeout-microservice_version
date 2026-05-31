package com.example.seckillservice.mq.sender;

import com.example.seckillservice.mq.message.SeckillOrderTimeoutMessage;
import com.example.seckillservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderTimeoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderTimeoutMessage(Long orderId){
        SeckillOrderTimeoutMessage message = new SeckillOrderTimeoutMessage();

        message.setOrderId(orderId);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SECKILL_ORDER_DELAY_EXCHANGE,
                RabbitMQConfig.SECKILL_ORDER_DELAY_ROUTING_KEY,
                message
        );

        System.out.println("发送订单超时消息：orderId=" + orderId + ", type=秒杀订单" + ", 将在15分钟后检查");

    }
}
