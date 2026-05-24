package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.entity.Category;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.es.DishDoc;
import com.example.fjm0313_takeout_self.es.repository.DishSearchRepository;
import com.example.fjm0313_takeout_self.mapper.CateGoryMapper;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.example.fjm0313_takeout_self.service.DishSearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishSearchServiceImpl implements DishSearchService {

    @Autowired
    private DishSearchRepository repository;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CateGoryMapper cateGoryMapper;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveDishToEs(Long dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null){
            return ;
        }
        DishDoc dishDoc = convertToDishDoc(dish);
        repository.save(dishDoc);
    }

    @Override
    public void deleteDishFromEs(Long dishId) {
        repository.deleteById(dishId);
    }

    @Override
    public void rebuildDishIndex() {
        repository.deleteAll();

        List<Dish> dishList = dishMapper.selectList(null);
        List<DishDoc> dishDocList = new ArrayList<>();

        for(Dish dish : dishList){
            dishDocList.add(convertToDishDoc(dish));
        }
        repository.saveAll(dishDocList);
    }

    @Override
    public List<DishDoc> search(String keyword) {

        // 构建查询
        if(!StringUtils.hasText(keyword)){
            return new ArrayList<>();
        }
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b.
                                should(s -> s.
                                        match(m -> m.
                                                field("name").
                                                query(keyword))).
                                should(s -> s.
                                        match(m -> m.
                                                field("description").
                                                query(keyword))).
                                should(s -> s.
                                        match(m -> m.
                                                field("categoryName").
                                                query(keyword)))


                        )
                ).withMaxResults(20)
                .build();

        // 查询并整合返回结果
        return elasticsearchOperations.search(query,DishDoc.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    private DishDoc convertToDishDoc(Dish dish){
        DishDoc dishDoc = new DishDoc();
        BeanUtils.copyProperties(dish,dishDoc);
        if(dish.getCategoryId() != null){
            Category category = cateGoryMapper.selectById(dish.getCategoryId());
            if(category != null){
                dishDoc.setCategoryName(category.getName());
            }
        }
        return dishDoc;
    }
}
