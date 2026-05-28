package com.example.userservice.service;


import com.example.userservice.entity.User;

public interface UserService {
    User findByUsername(String username);
    void register(User user);
    User findById(Long id);
}