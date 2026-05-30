package com.example.orderservice.mq.consumer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.orderservice.mq.message.OrderTimeoutMessage;
import com.example.orderservice.config.RabbitMQConfig;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.Orders;
import com.example.orderservice.entity.SeckillOrder;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.OrdersMapper;
import com.example.orderservice.mapper.SeckillActivityMapper;
import com.example.orderservice.mapper.SeckillOrderMapper;
import com.example.orderservice.service.DishService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTimeoutConsumer {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private DishService dishService;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @RabbitListener(queues = RabbitMQConfig.ORDER_DLX_QUEUE)
    @Transactional
    public void handleOrderTimeout(OrderTimeoutMessage message){

        Long orderId = message.getOrderId();
        String orderType = message.getOrderType();

        System.out.println("收到超时检查消息：orderId=" + orderId + ", type=" + orderType);

        if(orderType.equals("NORMAL")){
            handleNormalOrder(orderId);
        }else if(orderType.equals("SECKILL")){
            handleSeckillOrder(orderId);
        }

    }

    private void handleNormalOrder(Long orderId){
        Orders order = ordersMapper.selectById(orderId);
        if(order == null){
            return;
        }

        if(order.getStatus() != 0){
            System.out.println("订单" + orderId + "状态为" + order.getStatus() + "，无需取消");
            return;
        }

        order.setStatus(5);
        ordersMapper.updateById(order);

        List<OrderDetail> orderDetails = orderDetailMapper.selectList(
                new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId,orderId)
        );

        for(OrderDetail detail : orderDetails){
            dishService.restoreStock(detail.getDishId(),detail.getNumber());
        }

        System.out.println("普通订单超时取消成功：orderId=" + orderId);

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
