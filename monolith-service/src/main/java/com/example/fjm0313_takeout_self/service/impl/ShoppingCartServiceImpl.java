package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.mapper.ShoppingCartMapper;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper cartMapper;


    @Override
    public ShoppingCart findExistItem(Long userId, Long dishId, String flavor, String portion) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(ShoppingCart::getDishId, dishId);
        wrapper.eq(ShoppingCart::getFlavor, flavor);
        wrapper.eq(ShoppingCart::getPortion, portion);
        return cartMapper.selectOne(wrapper);
    }

    @Override
    public void updateCartItem(ShoppingCart cart) {
        cartMapper.updateById(cart);
    }

    @Override
    public void addCartItem(ShoppingCart cart) {
        cartMapper.insert(cart);
    }

    @Override
    public void removeCartItem(Long id) {
        cartMapper.deleteById(id);
    }

    @Override
    public List<ShoppingCart> findByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.orderByDesc(ShoppingCart::getCreateTime);
        return cartMapper.selectList(wrapper);
    }

    @Override
    public void clearByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        cartMapper.delete(wrapper);
    }
}