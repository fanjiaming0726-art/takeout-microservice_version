package com.example.seckillservice.mq.consumer;

import com.example.seckillservice.config.RabbitMQConfig;
import com.example.seckillservice.mq.message.SeckillOrderNotifyMessage;
import com.example.seckillservice.webSocket.SellerOrderWebSocketHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderNotifyConsumer {

    @Autowired
    private SellerOrderWebSocketHandler sellerOrderWebSocketHandler;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_NOTIFY_QUEUE)
    public void handleNewOrderMessage(SeckillOrderNotifyMessage message){
        System.out.println("收到新订单通知消息：orderId=" + message.getOrderId());

        sellerOrderWebSocketHandler.sendToAllSellers(message);
    }

}
