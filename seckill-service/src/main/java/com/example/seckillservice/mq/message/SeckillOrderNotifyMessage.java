package com.example.seckillservice.mq.message;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillOrderNotifyMessage {

    private Long orderId;

    private Long userId;

    private String username;

    private BigDecimal seckillPrice;

    private String consignee;

    private String phone;

    private String address;

    private String remark;

    private String orderNumber;

}
