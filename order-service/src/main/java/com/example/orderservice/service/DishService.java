package com.example.orderservice.service;

import com.example.orderservice.entity.Dish;

import java.util.List;

public interface DishService {
    void restoreStock(Long dihId,int count);
    void addDish(Dish dish);
    void deleteDishByIds(List<Long> ids);
    List<Dish> findAll();
    Dish findById(Long id);
    void updateDish(Dish dish);
    void deductStock(Long dishId, int count);
}