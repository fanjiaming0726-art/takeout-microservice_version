package com.example.fjm0313_takeout_self.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetail implements Serializable {
    private static final Long serialVersionUID = 1L;

    // id是为了与同类型的数据进行区分
    @TableId(type = IdType.AUTO)
    private Long id;

    // orderId是为了与order绑定
    private Long orderId;

    private String name;

    private String image;

    // 便于拉取菜名
    private Long dishId;

    private BigDecimal amount;

    private Integer number;

    // 客户的口味
    private String flavor;

    // 客户的份量
    private String portion;



}
