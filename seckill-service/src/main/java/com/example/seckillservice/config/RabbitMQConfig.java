package com.example.seckillservice.config;


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


    // ==================== 秒杀（已有） ====================
    // 交换机名称
    public static final String SECKILL_EXCHANGE = "seckill.exchange";

    // 队列名称
    public static final String SECKILL_ORDER_QUEUE = "seckill.order.queue";

    // 路由键
    public static final String SECKILL_ORDER_ROUTING_KEY = "seckill.order";


    @Bean
    public Queue seckillOrderQueue(){
        return new Queue(SECKILL_ORDER_QUEUE,true);
    }

    @Bean
    public DirectExchange seckillExchange(){
        return new DirectExchange(SECKILL_EXCHANGE);

    }

    @Bean
    public Binding seckillOrderBinding(@Qualifier("seckillOrderQueue") Queue seckillOrderQueue, @Qualifier("seckillExchange") DirectExchange seckillExchange){
        return BindingBuilder.bind(seckillOrderQueue).to(seckillExchange).with(SECKILL_ORDER_ROUTING_KEY);
    }

    // ==================== 秒杀订单延迟取消 ====================
    public static final String SECKILL_ORDER_DELAY_EXCHANGE =  "seckillOrder.delay.exchange";
    public static final String SECKILL_ORDER_DLX_EXCHANGE = "seckillOrder.dlx.exchange";

    public static final String SECKILL_ORDER_DELAY_QUEUE =  "seckillOrder.delay.queue";
    public static final String SECKILL_ORDER_DLX_QUEUE = "seckillOrder.dlx.queue";

    public static final String SECKILL_ORDER_DELAY_ROUTING_KEY = "seckillOrder.delay";
    public static final String SECKILL_ORDER_DLX_ROUTING_KEY = "seckillOrder.dlx";

    // 测试：30秒
    public static final int  SECKILL_ORDER_TTL = 30 * 1000;

    @Bean
    public DirectExchange seckillOrderDelayExchange (){
        return new DirectExchange(SECKILL_ORDER_DELAY_EXCHANGE);
    }

    @Bean
    public DirectExchange seckillOrderDlxExchange(){
        return new DirectExchange(SECKILL_ORDER_DLX_EXCHANGE);
    }


    @Bean
    public Queue seckillOrderDelayQueue() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("x-dead-letter-exchange",SECKILL_ORDER_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key",SECKILL_ORDER_DLX_ROUTING_KEY);
        args.put("x-message-ttl",SECKILL_ORDER_TTL);
        return QueueBuilder.durable(SECKILL_ORDER_DELAY_QUEUE).withArguments(args).build();

    }


    @Bean
    public Queue seckillOrderDlxQueue(){
        return new Queue(SECKILL_ORDER_DLX_QUEUE,true);
    }


    @Bean
    // bind queue to exchange with key
    public Binding seckilllOrderDelayBinding(@Qualifier("seckillOrderDelayQueue") Queue orderDelayQueue, @Qualifier("seckillOrderDelayExchange") DirectExchange orderDelayExchange) {
        return BindingBuilder.bind(orderDelayQueue).to(orderDelayExchange).with(SECKILL_ORDER_DELAY_ROUTING_KEY);
    }


    @Bean
    public Binding seckillOrderDlxBinding(@Qualifier("seckillOrderDlxQueue") Queue orderDlxQueue, @Qualifier("seckillOrderDlxExchange") DirectExchange orderDlxExchange) {
        return BindingBuilder.bind(orderDlxQueue).to(orderDlxExchange).with(SECKILL_ORDER_DLX_ROUTING_KEY);
    }

    // ==================== 商家端新订单通知 ====================
    public static final String SECKILL_ORDER_NOTIFY_EXCHANGE = "seckillOrder.notify.exchange";
    public static final String SECKILL_ORDER_NOTIFY_QUEUE  = "seckillOrder.notify.queue";
    public static final String SECKILL_ORDER_NOTIFY_ROUTING_KEY = "seckillOrder.notify";

    @Bean
    public DirectExchange seckillOrderNotifyExchange(){
        return new DirectExchange(SECKILL_ORDER_NOTIFY_EXCHANGE);
    }

    @Bean
    public Queue seckillOrderNotifyQueue(){
        return QueueBuilder.durable(SECKILL_ORDER_NOTIFY_QUEUE).build();
    }

    @Bean
    public Binding seckillOrderNotifyBinding(@Qualifier("seckillOrderNotifyQueue") Queue orderNotifyQueue, @Qualifier("seckillOrderNotifyExchange") DirectExchange orderNotifyExchange){
        return BindingBuilder.bind(orderNotifyQueue).to(orderNotifyExchange).with(SECKILL_ORDER_NOTIFY_ROUTING_KEY);
    }



    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
