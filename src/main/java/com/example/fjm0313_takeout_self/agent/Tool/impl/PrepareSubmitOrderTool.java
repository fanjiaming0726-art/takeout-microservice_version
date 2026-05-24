package com.example.fjm0313_takeout_self.agent.Tool.impl;

import com.example.fjm0313_takeout_self.agent.Tool.AgentContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentTool;
import com.example.fjm0313_takeout_self.agent.Tool.ToolResult;
import com.example.fjm0313_takeout_self.agent.pending.PendingAction;
import com.example.fjm0313_takeout_self.agent.pending.PendingActionStore;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.service.AddressBookService;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PrepareSubmitOrderTool implements AgentTool {

    private final ShoppingCartService shoppingCartService;
    private final AddressBookService addressBookService;
    private final PendingActionStore pendingActionStore;


    @Override
    public String name() {
        return "prepare_submit_order";
    }

    @Override
    public String description() {
        return "创建一个待确认的提交订单动作，不会直接下单";
    }

    /**
     * 给用户看菜单的信息，地址，总计金额，备注等等信息，然后设置待确认动作，最后返回此次订单结果
     * @param arguments
     * @param context
     * @return
     */
    @Override
    public ToolResult execute(JsonNode arguments, AgentContext context) {
        List<ShoppingCart> cartList = shoppingCartService.findByUserId(context.getUserId());

        if(cartList == null || cartList.isEmpty()){
            return ToolResult.fail("购物车为空，不能下单");
        }


        AddressBook addressBook = addressBookService.findDefaultByUserId(context.getUserId());
        if(addressBook == null){
            return ToolResult.fail("没有找到默认地址，请先设置默认地址");
        }

        String remark = text(arguments,"remark");
        if(StringUtils.hasText("remark")){
            remark = "无";
        }

        BigDecimal  total = BigDecimal.ZERO;
        for(ShoppingCart cart : cartList){
            total = total.add(cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber())));
        }

        String fullAddress = buildFullAddress(addressBook);

        String confirmText = "你当前购物车合计：" + total + "元，将使用默认地址：" + fullAddress + "备注：" + remark + "。确认提交订单吗？";


        PendingAction action = new PendingAction();
        action.setActionType("SUBMIT_ORDER");
        action.setUserId(context.getUserId());
        action.setAddressBookId(addressBook.getId());
        action.setConfirmText(confirmText);
        action.setRemark(remark);

        pendingActionStore.save(action);

        return ToolResult.success("已创建订单", Map.of(
                "actionType","SUBMIT_ORDER",
                "confirmText",confirmText,
                "total",total,
                "address",fullAddress,
                "remark",remark
        ));

    }

    private String text(JsonNode node, String field) {
        if (node == null || !node.hasNonNull(field)) {
            return null;
        }
        return node.get(field).asText();
    }

    private String buildFullAddress(AddressBook addressBook){
        return safe(addressBook.getProvinceName()) +
                safe(addressBook.getCityName()) +
                safe(addressBook.getDistrictName()) +
                safe(addressBook.getDistrictName());
    }


    private String safe(String value){
        return value == null ? "" : value;

    }
}