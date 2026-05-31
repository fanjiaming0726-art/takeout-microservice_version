package com.example.seckillservice.mq.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillMessage implements Serializable {
    private final static Long serialVersionUID = 1L;

    private Long activityId;

    private Long userId;
}
