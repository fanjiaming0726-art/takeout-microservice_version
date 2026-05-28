package com.example.fjm0313_takeout_self.common.mq.message;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderNotifyMessage {

    private Long orderId;

    private String orderNumber;

    private Long userId;

    private String username;

    private BigDecimal amount;

    private String consignee;

    private String phone;

    private String address;

    private String remark;


}
