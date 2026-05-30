package com.example.productservice.repository;

import com.example.productservice.entity.DishDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DishSearchRepository extends ElasticsearchRepository<DishDoc,Long> {
}
