package com.example.fjm0313_takeout_self.agent.service.impl;

import com.example.commonservice.context.UserContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentContext;
import com.example.fjm0313_takeout_self.agent.Tool.AgentTool;
import com.example.fjm0313_takeout_self.agent.Tool.AgentToolRegistry;
import com.example.fjm0313_takeout_self.agent.Tool.ToolResult;
import com.example.fjm0313_takeout_self.agent.config.DeepSeekAgentProperties;
import com.example.fjm0313_takeout_self.agent.dto.AgentChatRequest;
import com.example.fjm0313_takeout_self.agent.dto.AgentChatResponse;
import com.example.fjm0313_takeout_self.agent.dto.AgentDecision;
import com.example.fjm0313_takeout_self.agent.llm.DeepSeekAgentClient;
import com.example.fjm0313_takeout_self.agent.llm.DeepSeekAgentPromptBuilder;
import com.example.fjm0313_takeout_self.agent.pending.PendingAction;
import com.example.fjm0313_takeout_self.agent.pending.PendingActionStore;
import com.example.fjm0313_takeout_self.agent.service.AgentService;
import com.example.fjm0313_takeout_self.entity.Orders;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.service.OrdersService;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final ShoppingCartService shoppingCartService;
    private final PendingActionStore pendingActionStore;
    private final OrdersService ordersService;
    private final ObjectMapper objectMapper;
    private final AgentToolRegistry agentToolRegistry;
    private final DeepSeekAgentClient deepSeekAgentClient;

    private static final int MAX_AGENT_STEPS = 6;

    @Override
    public AgentChatResponse chat(AgentChatRequest request) {
        Long userId = UserContext.getUserId();
        String message =  request.getMessage();

        if(!StringUtils.hasText(message)){
            return AgentChatResponse.reply("你可以告诉我想吃什么，或者让我查看购物车，帮你下单。");
        }
        if(isConfirmMessage(message)){
            return executeLatestPendingAction(userId);
        }
        if(isCancelMessage(message)){
            pendingActionStore.removeLatestByUserId(userId);
            return AgentChatResponse.reply("好的，我已经取消了刚才的待确认动作");
        }

        ArrayNode messages = objectMapper.createArrayNode();

        messages.add(objectMapper.createObjectNode()
                .put("role","system")
                .put("content", DeepSeekAgentPromptBuilder.buildSystemPrompt()));

        messages.add(objectMapper.createObjectNode()
                .put("role","user")
                .put("content",DeepSeekAgentPromptBuilder.buildUserPrompt(message)));

        AgentContext context = new AgentContext(userId);
        for(int i = 0; i < MAX_AGENT_STEPS; i++){
            AgentDecision decision = deepSeekAgentClient.decide(messages);
            if(decision.isFinal()){
                return AgentChatResponse.reply(decision.getReply());
            }

            if(!decision.isToolCall()){
                return AgentChatResponse.reply("我暂时没有理解你的意思，你可以说，帮我点一份宫保鸡丁");
            }

            AgentTool tool = agentToolRegistry.getAgentTool(decision.getToolName());

            if(tool == null){
                return AgentChatResponse.reply("系统暂时不支持这个工具：" + decision.getToolName());
            }
            ToolResult toolResult = tool.execute(decision.getArguments(),context);

            if("pre_add_cart".equals(decision.getToolName()) || "pre_submit_order".equals(decision.getToolName())){
                return buildConfirmResponse(toolResult);
            }
            String toolResultJson;
            try {
                toolResultJson = objectMapper.writeValueAsString(toolResult);
            } catch (JsonProcessingException e) {
                toolResultJson = "{\"success\":false,\"message\":\"工具结果序列化失败\"}";
            }
            messages.add(objectMapper.createObjectNode()
                    .put("role","assistant")
                    .put("content",decision.getRawContent())
            );

            messages.add(objectMapper.createObjectNode()
                    .put("role","user")
                    .put("content",DeepSeekAgentPromptBuilder.buildToolResultPrompt(
                            decision.getToolName(),
                            toolResultJson
                    )));

        }

        return AgentChatResponse.reply("我已经尝试了多步操作，但还没有完成任务，你还可以换一种说法再试一次");


    }

    private AgentChatResponse buildConfirmResponse(ToolResult toolResult){
       if(!Boolean.TRUE.equals(toolResult.getSuccess())){
           return AgentChatResponse.reply(toolResult.getMessage());
       }

       Map<String, Object> data = toolResult.getData();

        try {
            String json = objectMapper.writeValueAsString(data);
            JsonNode node = objectMapper.readTree(json);

            String confirmText = node.path("confirmText").asText("请确认是否继续");
            String actionType = node.path("actionType").asText();

            return AgentChatResponse.needConfirm(confirmText,actionType);

        } catch (JsonProcessingException e) {
            return AgentChatResponse.reply("待确认动作创建成功，但响应失败");
        }

    }



    private AgentChatResponse executeLatestPendingAction(Long userId){
        PendingAction action = pendingActionStore.getLatestPendingAction(userId);

        if(action == null){
            return AgentChatResponse.reply("目前没有需要确认的操作");
        }
        if("ADD_CART".equals(action.getActionType())){
            doAddCart(action);
            pendingActionStore.removeLatestByUserId(userId);
            return AgentChatResponse.reply("已帮你把[" + action.getDishName() + "]加入购物车");
        }
        if("SUBMIT_ORDER".equals(action.getActionType())){
            Orders orders = ordersService.submitOrder(
                    action.getUserId(),
                    action.getAddressBookId(),
                    action.getRemark()
            );
            pendingActionStore.removeLatestByUserId(userId);
            return AgentChatResponse.reply("订单已提交，订单号：" + orders.getNumber() + "，金额：" + orders.getAmount() + "元。");

        }
        return AgentChatResponse.reply("这个待确认操作暂时不支持");
    }


    private void doAddCart(PendingAction action){

        int quantity = action.getQuantity() == null || action.getQuantity() <= 0 ? 1 : action.getQuantity();

        ShoppingCart cart = new ShoppingCart();

        cart.setUserId(action.getUserId());
        cart.setDishId(action.getDishId());
        cart.setName(action.getDishName());
        cart.setImage(action.getImage());
        cart.setAmount(action.getAmount());
        cart.setFlavor(action.getFlavor());
        cart.setPortion(action.getPortion());
        cart.setNumber(quantity);
        cart.setCreateTime(LocalDateTime.now());

        shoppingCartService.addCartItem(cart);
    }



    private boolean isCancelMessage(String message){
        String text = message.trim();
        return text.equals("取消") || text.equals("算了") || text.equals("不要了") || text.equals("先不点了") || text.equals("撤销");
    }

    private boolean isConfirmMessage(String message){
        String text = message.trim();
        return text.equals("确认") || text.equals("可以") || text.equals("好的") || text.equals("好") || text.equals("行") || text.equals("可以的") || text.equals("没问题") || text.equals("就这样") || text.equals("提交吧") || text.equals("加吧");
    }

}
