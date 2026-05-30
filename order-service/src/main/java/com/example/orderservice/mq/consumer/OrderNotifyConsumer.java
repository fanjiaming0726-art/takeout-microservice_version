package com.example.orderservice.mq.consumer;

import com.example.orderservice.mq.message.OrderNotifyMessage;
import com.example.orderservice.websocket.SellerOrderWebSocketHandler;
import com.example.orderservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderNotifyConsumer {

    @Autowired
    private SellerOrderWebSocketHandler sellerOrderWebSocketHandler;

    @RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFY_QUEUE)
    public void handleNewOrderMessage(OrderNotifyMessage message){
        System.out.println("收到新订单通知消息：orderId=" + message.getOrderId());

        sellerOrderWebSocketHandler.sendToAllSellers(message);
    }

}
