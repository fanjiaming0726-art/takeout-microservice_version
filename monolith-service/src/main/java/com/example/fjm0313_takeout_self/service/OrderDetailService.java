package com.example.fjm0313_takeout_self.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void saveOrderDetails(List<OrderDetail> details);
    List<OrderDetail> findByOrderId(Long orderId);
}