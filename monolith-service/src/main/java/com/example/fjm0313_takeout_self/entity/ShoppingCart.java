package com.example.fjm0313_takeout_self.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.cglib.core.Local;

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
