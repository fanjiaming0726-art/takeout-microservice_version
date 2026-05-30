package com.example.orderservice.repository;

import com.example.orderservice.entity.DishDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DishSearchRepository extends ElasticsearchRepository<DishDoc,Long> {
}
