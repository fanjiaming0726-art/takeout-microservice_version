package com.example.fjm0313_takeout_self.agent.Tool.impl;

import com.example.fjm0313_takeout_self.agent.Tool.AgentContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentTool;
import com.example.fjm0313_takeout_self.agent.Tool.ToolResult;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetCartTool implements AgentTool {


    /**
     * 获取购物车信息
     * 1. 菜品栏目
     * 2. 总价格
     * 且以json数据格式返回
     */

    private final  ShoppingCartService shoppingCartService;

    @Override
    public String name(){
        return "get_cart";
    }

    @Override
    public String description(){
        return "查询当前用户购物车";
    }

    @Override
    public ToolResult execute(JsonNode arguments, AgentContext context) {
        List<ShoppingCart> cartList = shoppingCartService.findByUserId(context.getUserId());
        if(cartList == null || cartList.isEmpty()){
            return ToolResult.success(
                    "购物车为空",
                    Map.of(
                       "items",List.of(),
                       "total", BigDecimal.ZERO
                    ));
        }
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total =  BigDecimal.ZERO;
        for(ShoppingCart cart : cartList){
            BigDecimal itemTotal = cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber()));
            total = total.add(itemTotal);

            items.add(Map.of(
                    "cartId",cart.getId(),
                    "dishId",cart.getDishId(),
                    "name",cart.getName(),
                    "number",cart.getNumber(),
                    "amount",cart.getAmount(),
                    "flavor",cart.getFlavor() == null ? "默认" : cart.getFlavor(),
                    "portion",cart.getPortion() == null ? "标准" : cart.getPortion(),
                    "itemTotal",itemTotal
            ));

        }
        return ToolResult.success("查询购物车成功",Map.of(
                "items",items,
                "total",total
        ));

    }


}
