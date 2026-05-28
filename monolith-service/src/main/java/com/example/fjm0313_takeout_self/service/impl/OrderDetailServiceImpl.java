package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.entity.OrderDetail;
import com.example.fjm0313_takeout_self.mapper.OrderDetailMapper;
import com.example.fjm0313_takeout_self.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    public void saveOrderDetails(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            orderDetailMapper.insert(detail);
        }
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        return orderDetailMapper.selectList(wrapper);
    }
}