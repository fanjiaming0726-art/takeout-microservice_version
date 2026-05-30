package com.example.productservice.service;

import com.example.productservice.entity.Category;

import java.util.List;

public interface CateGoryService {
    void addCategory(Category category);
    void updateCategory(Category category);
    Category findById(Long id);
    List<Category> findAll();
}