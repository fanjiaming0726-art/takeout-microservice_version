package com.example.fjm0313_takeout_self.agent.Tool;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AgentToolRegistry {

    private final Map<String, AgentTool> toolMap = new ConcurrentHashMap<>();

    // 这个构造方法会在Spring创建Bean容器的时候执行
    public AgentToolRegistry(List<AgentTool> tools){
        for(AgentTool tool : tools){
            toolMap.put(tool.name(),tool);
        }
    }
    public AgentTool getAgentTool(String name){
        return toolMap.get(name);
    }


}
