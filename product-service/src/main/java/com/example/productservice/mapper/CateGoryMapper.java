package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CateGoryMapper extends BaseMapper<Category> {
}
