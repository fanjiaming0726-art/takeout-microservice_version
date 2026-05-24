package com.example.fjm0313_takeout_self.agent.service;

import com.example.fjm0313_takeout_self.agent.dto.AgentChatRequest;
import com.example.fjm0313_takeout_self.agent.dto.AgentChatResponse;

public interface AgentService {
    AgentChatResponse chat(AgentChatRequest request);
}
