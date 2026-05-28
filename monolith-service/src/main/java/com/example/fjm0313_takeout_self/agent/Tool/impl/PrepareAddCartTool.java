package com.example.fjm0313_takeout_self.agent.Tool.impl;

import com.example.fjm0313_takeout_self.agent.Tool.AgentContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentTool;
import com.example.fjm0313_takeout_self.agent.Tool.ToolResult;
import com.example.fjm0313_takeout_self.agent.pending.PendingAction;
import com.example.fjm0313_takeout_self.agent.pending.PendingActionStore;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PrepareAddCartTool implements AgentTool {

    private final DishMapper dishMapper;
    private final PendingActionStore pendingActionStore;

    @Override
    public String name() {
        return "prepare_add_cart";
    }

    @Override
    public String description() {
        return "创建一个待加入购物车的动作，但是不会立即修改购物车";
    }

    @Override
    public ToolResult execute(JsonNode arguments, AgentContext context) {
        if(arguments == null || !arguments.hasNonNull("dishId")){
            return ToolResult.fail("缺少dishId，不能准备加入购物车");
        }

        Long dishId = arguments.get("dishId").asLong();
        Dish dish = dishMapper.selectById(dishId);

        if(dish == null){
            return ToolResult.fail("没有找到这个菜品");
        }
        if(dish.getStatus() == null || dish.getStatus() != 1){
            return ToolResult.fail("此菜品当前不可售");
        }

        Integer quantity = arguments.hasNonNull("quantity") ? arguments.get("quantity").asInt() : 1;
        if(quantity <= 0){
            quantity = 1;
        }

        String flavor = text(arguments,"flavor");
        if(!StringUtils.hasText(flavor)){
            flavor = "默认";
        }

        String portion = text(arguments,"portion");
        if(!StringUtils.hasText(portion)){
            portion = "标准";
        }

        PendingAction action = new PendingAction();
        action.setActionType("ADD_CART");
        action.setUserId(context.getUserId());
        action.setDishId(dishId);
        action.setDishName(dish.getName());
        action.setImage(dish.getImage());
        action.setAmount(dish.getPrice());
        action.setQuantity(quantity);
        action.setFlavor(flavor);

        String confirmText = "我准备帮你把[" +dish.getName() + "]加入购物车，数量 " + quantity + ",口味：" + flavor + "份量：" + portion + "。确认吗";

        action.setConfirmText(confirmText);

        pendingActionStore.save(action);

        return ToolResult.success("已创建待确认的加入购物车动作", Map.of(
                "actionType","ADD_CART",
                "confirmText",confirmText,
                "dishName",dish.getName(),
                "quantity",quantity,
                "flavor",flavor,
                "portion",portion
        ));

    }

    private String text(JsonNode node, String field) {
        if (node == null || !node.hasNonNull(field)) {
            return null;
        }
        return node.get(field).asText();
    }
}
