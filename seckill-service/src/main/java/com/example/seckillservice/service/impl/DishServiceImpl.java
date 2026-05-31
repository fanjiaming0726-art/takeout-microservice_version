package com.example.seckillservice.service.impl;

import com.example.seckillservice.entity.Dish;
import com.example.seckillservice.mapper.DishMapper;
import com.example.seckillservice.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private final static String DISH_LIST_KEY = "dish:list";

    @Override
    public void restoreStock(Long dishId, int count) {
        dishMapper.restoreStock(dishId,count);
        redisTemplate.delete(DISH_LIST_KEY);

    }


    @Override
    public Dish findById(Long id) {
       return dishMapper.selectById(id);
    }



    @Override
    public void deductStock(Long dishId, int count) {
        Dish dish = dishMapper.selectById(dishId);
        if(dish == null){
            throw new RuntimeException("菜品不存在");
        }
        if(dish.getStock() < count){
            throw new RuntimeException(dish.getName() + "库存不足，剩余：" + dish.getStock());
        }

        int rows = dishMapper.deductStock(dishId,count,dish.getVersion());

        if(rows == 0){
            dish = dishMapper.selectById(dishId);

            if(dish.getStock() < count){
                throw new RuntimeException(dish.getName() + "库存不足，剩余：" + dish.getStock());
            }

            rows = dishMapper.deductStock(dishId,count,dish.getVersion());
            if(rows == 0){
                throw new RuntimeException("系统繁忙，请稍后再试");
            }
        }
        redisTemplate.delete(DISH_LIST_KEY);
    }
}