package com.example.fjm0313_takeout_self.common.MQ.consumer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.MQ.message.SeckillMessage;
import com.example.fjm0313_takeout_self.common.MQ.sender.OrderTimeoutSender;
import com.example.fjm0313_takeout_self.config.RabbitMQConfig;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.entity.SeckillActivity;
import com.example.fjm0313_takeout_self.entity.SeckillOrder;
import com.example.fjm0313_takeout_self.entity.User;
import com.example.fjm0313_takeout_self.mapper.AddressBookMapper;
import com.example.fjm0313_takeout_self.mapper.SeckillActivityMapper;
import com.example.fjm0313_takeout_self.mapper.SeckillOrderMapper;
import com.example.fjm0313_takeout_self.service.RankingService;
import com.example.fjm0313_takeout_self.service.SeckillService;
import com.example.fjm0313_takeout_self.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SeckillOrderConsumer {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper ;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private OrderTimeoutSender orderTimeoutSender;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_QUEUE)
    @Transactional
    public void handleSeckillOrder(SeckillMessage message){
        try {
            Long activityId = message.getActivityId();
            Long userId = message.getUserId();

            SeckillActivity activity = seckillService.findById(activityId);
            seckillActivityMapper.deductStock(activityId);

            // 查用户和默认地址
            User user = userService.findById(userId);
            AddressBook addressBook = addressBookMapper.selectOne(
                    new LambdaQueryWrapper<AddressBook>()
                            .eq(AddressBook::getUserId, userId)
                            .eq(AddressBook::getIsDefault, 1)
            );

            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setUserId(userId);
            seckillOrder.setUsername(user.getUsername());
            seckillOrder.setActivityId(activityId);
            seckillOrder.setDishId(activity.getDishId());
            seckillOrder.setDishName(activity.getDishName());
            seckillOrder.setSeckillPrice(activity.getSeckillPrice());
            seckillOrder.setOrderNumber(UUID.randomUUID().toString().replace("-", ""));
            seckillOrder.setStatus(0);

            if (addressBook != null) {
                seckillOrder.setConsignee(addressBook.getConsignee());
                seckillOrder.setPhone(addressBook.getPhone());
                String fullAddress = (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail() == null ? "" : addressBook.getDetail());
                seckillOrder.setAddress(fullAddress);
            }

            seckillOrderMapper.insert(seckillOrder);

            orderTimeoutSender.sendOrderTimeoutMessage(seckillOrder.getId(),"SECKILL");

            rankingService.increase(activity.getDishId(), activity.getDishName(), 1);

            System.out.println("秒杀订单创建成功，userId=" + userId + ", activityId=" + activityId);


        }catch (Exception e){
            System.out.println("秒杀订单处理失败：" + e.getMessage());
            throw e;
        }
    }

}
