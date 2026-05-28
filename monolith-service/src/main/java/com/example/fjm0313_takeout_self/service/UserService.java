package com.example.fjm0313_takeout_self.service;


import com.example.fjm0313_takeout_self.entity.User;

public interface UserService {
    User findByUsername(String username);
    void register(User user);
    User findById(Long id);
}