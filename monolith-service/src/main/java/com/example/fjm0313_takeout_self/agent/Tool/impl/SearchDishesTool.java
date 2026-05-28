package com.example.fjm0313_takeout_self.agent.Tool.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.agent.Tool.AgentContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentTool;
import com.example.fjm0313_takeout_self.agent.Tool.ToolResult;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SearchDishesTool implements AgentTool {

    private final DishMapper dishMapper;

    @Override
    public String name() {
        return "search_dishes";
    }

    @Override
    public String description() {
        return "根据菜名，口味，偏好，价格等条件真实查询菜品";
    }


    /**
     * 从需求中提取所需菜品的名字/口味/偏好，与数据库中的菜品逐一比较，名字必须符合，口味和偏好符合就筛选出来，不符合就算了
     * @param arguments
     * @param context
     * @return
     */

    @Override
    public ToolResult execute(JsonNode arguments, AgentContext context) {
        String keyword = text(arguments,"keyword");
        String flavor = text(arguments,"flavor");
        String preference = text(arguments,"preference");

        // 1. 有菜可查
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus,1);

        if(StringUtils.hasText(keyword)){
            wrapper.and(w->w
                    .like(Dish::getName,keyword)
                    .or()
                    .like(Dish::getDescription,keyword)
            );
        }
        if(StringUtils.hasText(flavor)){
            wrapper.or(w->w
                    .like(Dish::getName,flavor)
                    .or()
                    .like(Dish::getDescription,flavor)

            );
        }
        if(StringUtils.hasText(preference)){
            wrapper.or(w->w
                    .like(Dish::getName,preference)
                    .or()
                    .like(Dish::getDescription,preference)

            );
        }
        BigDecimal maxPrice;
        if(arguments.hasNonNull("maxPrice")){
            maxPrice = arguments.get("maxPrice").decimalValue();
            wrapper.le(Dish::getPrice,maxPrice);
        }

        int limit = 5;
        if(arguments.hasNonNull("limit")){
            limit = Math.max(1, Math.min(arguments.get("limit").asInt(),10));
        }
        wrapper.last("limit " + limit);

        List<Dish> dishes = dishMapper.selectList(wrapper);

        // 没菜可查
        if(dishes == null || dishes.isEmpty()){
            LambdaQueryWrapper<Dish> fallback = new LambdaQueryWrapper<>();
            fallback.eq(Dish::getStatus,1);
            fallback.last("limit " + limit);
            dishes = dishMapper.selectList(wrapper);
        }

        // 创建最终结果
        List<Map<String, Object>> data = new ArrayList<>();
        for(Dish dish : dishes){
            data.add(
                 Map.of(
                    "dishId",dish.getId(),
                    "name",dish.getName(),
                    "price",dish.getPrice(),
                    "stock",dish.getStock(),
                    "description",dish.getDescription() == null ? "" : dish.getDescription(),
                    "image",dish.getImage() == null ? "" : dish.getImage()
                 )
            );
        }
        return ToolResult.success("查询菜品成功",Map.of(
                "data",data,
                "count",data.size()
        ));


    }

    private String text(JsonNode node, String field){
        if(node == null || !node.hasNonNull(field)){
            return null;
        }
        String value = node.get(field).asText();
        return StringUtils.hasText(value) ? value : null;

    }
}
