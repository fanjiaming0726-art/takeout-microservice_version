package com.example.orderservice.service;


import com.example.orderservice.entity.DishDoc;

import java.util.List;

public interface DishSearchService {

    void saveDishToEs(Long dishId);

    void deleteDishFromEs(Long dishId);

    void rebuildDishIndex();

    List<DishDoc> search(String keyword);
}
