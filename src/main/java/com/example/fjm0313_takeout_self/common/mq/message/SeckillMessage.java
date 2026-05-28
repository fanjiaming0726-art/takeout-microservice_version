package com.example.fjm0313_takeout_self.common.mq.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillMessage implements Serializable {
    private final static Long serialVersionUID = 1L;

    private Long activityId;

    private Long userId;
}
