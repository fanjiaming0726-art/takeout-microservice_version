package com.example.seckillservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.seckillservice.entity.User;
import com.example.seckillservice.mapper.UserMapper;
import com.example.seckillservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
}