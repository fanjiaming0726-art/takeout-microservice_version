package com.example.orderservice.service;

import com.example.orderservice.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void saveOrderDetails(List<OrderDetail> details);
    List<OrderDetail> findByOrderId(Long orderId);
}