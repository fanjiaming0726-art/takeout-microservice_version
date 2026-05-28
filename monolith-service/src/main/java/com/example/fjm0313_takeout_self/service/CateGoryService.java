package com.example.fjm0313_takeout_self.service;

import com.example.fjm0313_takeout_self.entity.Category;

import java.util.List;

public interface CateGoryService {
    void addCategory(Category category);
    void updateCategory(Category category);
    Category findById(Long id);
    List<Category> findAll();
}