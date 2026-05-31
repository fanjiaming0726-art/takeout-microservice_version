package com.example.seckillservice.service;


import com.example.seckillservice.entity.User;

public interface UserService {
    User findByUsername(String username);
    User findById(Long id);
}