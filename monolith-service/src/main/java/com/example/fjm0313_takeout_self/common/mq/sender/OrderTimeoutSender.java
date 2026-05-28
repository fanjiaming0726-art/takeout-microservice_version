package com.example.fjm0313_takeout_self.common.mq.sender;

import com.example.fjm0313_takeout_self.common.mq.message.OrderTimeoutMessage;
import com.example.fjm0313_takeout_self.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTimeoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderTimeoutMessage(Long orderId, String orderType){
        OrderTimeoutMessage message = new OrderTimeoutMessage();

        message.setOrderId(orderId);
        message.setOrderType(orderType);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_DELAY_EXCHANGE,
                RabbitMQConfig.ORDER_DELAY_ROUTING_KEY,
                message
        );

        System.out.println("发送订单超时消息：orderId=" + orderId + ", type=" + orderType + ", 将在15分钟后检查");

    }
}
