package com.example.orderservice.service;

import java.util.List;
import java.util.Map;

public interface RankingService {
    void increase(Long dishId, String dishName,int count);
    List<Map<String,Object>> getTopN(int n);
}
