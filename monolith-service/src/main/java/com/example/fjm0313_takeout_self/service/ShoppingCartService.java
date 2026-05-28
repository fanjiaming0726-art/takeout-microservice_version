package com.example.fjm0313_takeout_self.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart findExistItem(Long userId, Long dishId, String flavor, String portion);
    void updateCartItem(ShoppingCart cart);
    void addCartItem(ShoppingCart cart);
    void removeCartItem(Long id);
    List<ShoppingCart> findByUserId(Long userId);
    void clearByUserId(Long userId);
}