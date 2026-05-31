package com.example.seckillservice.service;

import com.example.seckillservice.entity.Dish;


public interface DishService {
    void restoreStock(Long dihId,int count);
    Dish findById(Long id);
    void deductStock(Long dishId, int count);
}