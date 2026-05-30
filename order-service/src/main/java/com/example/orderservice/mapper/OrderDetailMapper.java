package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
