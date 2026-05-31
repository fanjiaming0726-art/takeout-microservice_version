package com.example.seckillservice.mq.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillOrderTimeoutMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
}
