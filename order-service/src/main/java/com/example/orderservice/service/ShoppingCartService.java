package com.example.orderservice.service;

import com.example.orderservice.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart findExistItem(Long userId, Long dishId, String flavor, String portion);
    void updateCartItem(ShoppingCart cart);
    void addCartItem(ShoppingCart cart);
    void removeCartItem(Long id);
    List<ShoppingCart> findByUserId(Long userId);
    void clearByUserId(Long userId);
}