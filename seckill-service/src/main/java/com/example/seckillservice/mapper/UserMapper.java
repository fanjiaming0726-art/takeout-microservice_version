package com.example.seckillservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckillservice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
