package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CateGoryMapper extends BaseMapper<Category> {
}
