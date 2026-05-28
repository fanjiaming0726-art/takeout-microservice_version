package com.example.fjm0313_takeout_self.agent.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class AgentDecision {

    private String type;

    public String toolName;

    private JsonNode arguments;

    private String reply;

    private String rawContent;

    public boolean isToolCall(){
        return "tool_call".equals(type);
    }

    public boolean isFinal(){
        return "final".equals(type);
    }


}
