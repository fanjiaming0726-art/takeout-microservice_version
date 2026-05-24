package com.example.fjm0313_takeout_self.agent.Tool;

import com.fasterxml.jackson.databind.JsonNode;

public interface AgentTool {

    String name();

    String description();

    // JsonNode其实就是Json字符串在被转换成Java内的树形对象的根节点
    ToolResult execute(JsonNode arguments,AgentContext context);

}
