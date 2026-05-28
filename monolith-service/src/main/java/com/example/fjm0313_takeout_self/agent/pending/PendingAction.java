package com.example.fjm0313_takeout_self.agent.pending;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PendingAction {

    private String actionType;

    private Long userId;

    private Long dishId;
    private String dishName;
    private String image;
    private BigDecimal amount;
    private Integer quantity;
    private String flavor;
    private String portion;

    private Long addressBookId;
    private String remark;


    private String confirmText;



}
