package com.example.orderservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {
    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long dishId;

    private String name;

    private String image;

    private Integer number;

    private BigDecimal amount;

    private String flavor;

    private String portion;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
