package com.example.fjm0313_takeout_self.vo;

import com.example.fjm0313_takeout_self.entity.OrderDetail;
import com.example.fjm0313_takeout_self.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersVO extends Orders {
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
