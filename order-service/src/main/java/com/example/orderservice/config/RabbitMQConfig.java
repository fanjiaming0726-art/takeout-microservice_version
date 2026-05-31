package com.example.orderservice.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfig {


    // ==================== 订单延迟取消 ====================
    public static final String NORMAL_ORDER_DELAY_EXCHANGE =  "normalOrder.delay.exchange";
    public static final String NORMAL_ORDER_DLX_EXCHANGE = "normalOrder.dlx.exchange";

    public static final String NORMAL_ORDER_DELAY_QUEUE =  "normalOrder.delay.queue";
    public static final String NORMAL_ORDER_DLX_QUEUE = "normalOrder.dlx.queue";

    public static final String NORMAL_ORDER_DELAY_ROUTING_KEY = "normalOrder.delay";
    public static final String NORMAL_ORDER_DLX_ROUTING_KEY = "normalOrder.dlx";

    // 测试：30秒
    public static final int  NORMAL_ORDER_TTL = 30 * 1000;

    @Bean
    public DirectExchange normalOrderDelayExchange (){
        return new DirectExchange(NORMAL_ORDER_DELAY_EXCHANGE);
    }

    @Bean
    public DirectExchange normalOrderDlxExchange(){
        return new DirectExchange(NORMAL_ORDER_DLX_EXCHANGE);
    }


    @Bean
    public Queue normalOrderDelayQueue() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("x-dead-letter-exchange",NORMAL_ORDER_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key",NORMAL_ORDER_DLX_ROUTING_KEY);
        args.put("x-message-ttl",NORMAL_ORDER_TTL);
        return QueueBuilder.durable(NORMAL_ORDER_DELAY_QUEUE).withArguments(args).build();

    }


    @Bean
    public Queue normalOrderDlxQueue(){
        return new Queue(NORMAL_ORDER_DLX_QUEUE,true);
    }


    @Bean
    // bind queue to exchange with key
    public Binding normalOrderDelayBinding(@Qualifier("normalOrderDelayQueue") Queue orderDelayQueue, @Qualifier("normalOrderDelayExchange") DirectExchange orderDelayExchange) {
        return BindingBuilder.bind(orderDelayQueue).to(orderDelayExchange).with(NORMAL_ORDER_DELAY_ROUTING_KEY);
    }


    @Bean
    public Binding normalOrderDlxBinding(@Qualifier("normalOrderDlxQueue") Queue orderDlxQueue, @Qualifier("normalOrderDlxExchange") DirectExchange orderDlxExchange) {
        return BindingBuilder.bind(orderDlxQueue).to(orderDlxExchange).with(NORMAL_ORDER_DLX_ROUTING_KEY);
    }

    // ==================== 商家端新订单通知 ====================
    public static final String NORMAL_ORDER_NOTIFY_EXCHANGE = "normalOrder.notify.exchange";
    public static final String NORMAL_ORDER_NOTIFY_QUEUE  = "normalOrder.notify.queue";
    public static final String NORMAL_ORDER_NOTIFY_ROUTING_KEY = "normalOrder.notify";

    @Bean
    public DirectExchange normalOrderNotifyExchange(){
        return new DirectExchange(NORMAL_ORDER_NOTIFY_EXCHANGE);
    }

    @Bean
    public Queue normalOrderNotifyQueue(){
        return QueueBuilder.durable(NORMAL_ORDER_NOTIFY_QUEUE).build();
    }

    @Bean
    public Binding normalOrderNotifyBinding(@Qualifier("normalOrderNotifyQueue") Queue orderNotifyQueue, @Qualifier("normalOrderNotifyExchange") DirectExchange orderNotifyExchange){
        return BindingBuilder.bind(orderNotifyQueue).to(orderNotifyExchange).with(NORMAL_ORDER_NOTIFY_ROUTING_KEY);
    }



    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
