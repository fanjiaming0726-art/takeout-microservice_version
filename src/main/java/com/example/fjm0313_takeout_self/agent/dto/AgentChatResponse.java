package com.example.fjm0313_takeout_self.agent.dto;

import lombok.Data;

@Data
public class AgentChatResponse {

    private String reply;

    private Boolean needConfirm;

    private String actionType;



    // 届时与用户对话时会有两种情况，一种是不用等用户确认，例如推荐菜品这类i，还有一种是得等用户确认，例如等用户确认下单
    public static AgentChatResponse reply(String reply){
        AgentChatResponse response = new AgentChatResponse();
        response.setReply(reply);
        response.setNeedConfirm(false);
        return response;

    }

    public static AgentChatResponse needConfirm(String reply,String actionType){
        AgentChatResponse response = new AgentChatResponse();
        response.setReply(reply);
        response.setNeedConfirm(true);
        response.setActionType(actionType);
        return response;
    }

}
