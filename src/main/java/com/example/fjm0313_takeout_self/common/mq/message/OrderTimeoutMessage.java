package com.example.fjm0313_takeout_self.common.mq.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderTimeoutMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;

    private String orderType;
}
