package com.example.fjm0313_takeout_self.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cglib.core.Converter;
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
    public Binding seckillOrderBinding(Queue seckillOrderQueue, DirectExchange seckillExchange){
        return BindingBuilder.bind(seckillOrderQueue).to(seckillExchange).with(SECKILL_ORDER_ROUTING_KEY);
    }

    // ==================== 订单延迟取消 ====================
    public static final String ORDER_DELAY_EXCHANGE =  "order.delay.exchange";
    public static final String ORDER_DLX_EXCHANGE = "order.dlx.exchange";

    public static final String ORDER_DELAY_QUEUE =  "order.delay.queue";
    public static final String ORDER_DLX_QUEUE = "order.dlx.queue";

    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay";
    public static final String ORDER_DLX_ROUTING_KEY = "order.dlx";

    // 测试：30秒
    public static final int  ORDER_TTL = 30 * 1000;

    @Bean
    public DirectExchange orderDelayExchange (){
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }

    @Bean
    public DirectExchange orderDlxExchange(){
        return new DirectExchange(ORDER_DLX_EXCHANGE);
    }


    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("x-dead-letter-exchange",ORDER_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key",ORDER_DLX_ROUTING_KEY);
        args.put("x-message-ttl",ORDER_TTL);
        return QueueBuilder.durable(ORDER_DELAY_QUEUE).withArguments(args).build();

    }


    @Bean
    public Queue orderDlxQueue(){
        return new Queue(ORDER_DLX_QUEUE,true);
    }


    @Bean
    // bind queue to exchange with key
    public Binding orderDelayBinding(Queue orderDelayQueue, DirectExchange orderDelayExchange) {
        return BindingBuilder.bind(orderDelayQueue).to(orderDelayExchange).with(ORDER_DELAY_ROUTING_KEY);
    }


    @Bean
    public Binding orderDlxBinding(Queue orderDlxQueue, DirectExchange orderDlxExchange) {
        return BindingBuilder.bind(orderDlxQueue).to(orderDlxExchange).with(ORDER_DLX_ROUTING_KEY);
    }

    // ==================== 商家端新订单通知 ====================
    public static final String ORDER_NOTIFY_EXCHANGE = "order.notify.exchange";
    public static final String ORDER_NOTIFY_QUEUE  = "order.notify.queue";
    public static final String ORDER_NOTIFY_ROUTINE_KEY = "order.notify";

    @Bean
    public DirectExchange orderNotifyExchange(){
        return new DirectExchange(ORDER_NOTIFY_EXCHANGE);
    }

    @Bean
    public Queue orderNotifyQueue(){
        return QueueBuilder.durable(ORDER_NOTIFY_QUEUE).build();
    }

    @Bean
    public Binding orderNotifyBinding(Queue orderNotifyQueue, DirectExchange orderNotifyExchange){
        return BindingBuilder.bind(orderNotifyQueue).to(orderNotifyExchange).with(ORDER_NOTIFY_ROUTINE_KEY);
    }



    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
