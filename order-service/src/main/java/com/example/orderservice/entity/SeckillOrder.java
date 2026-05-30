package com.example.orderservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillOrder implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String username;

    private Long activityId;

    private Long dishId;

    private String dishName;

    private BigDecimal seckillPrice;

    private String consignee;

    private String phone;

    private String address;

    private Integer status;  // 0未支付 1已支付 2已取消

    // 订单编号
    private String orderNumber;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
