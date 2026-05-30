package com.example.orderservice.vo;

import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersVO extends Orders {
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
