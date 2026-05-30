package com.example.productservice.service;


import com.example.productservice.entity.DishDoc;

import java.util.List;

public interface DishSearchService {

    void saveDishToEs(Long dishId);

    void deleteDishFromEs(Long dishId);

    void rebuildDishIndex();

    List<DishDoc> search(String keyword);
}
