package com.example.fjm0313_takeout_self.agent.controller;


import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.fjm0313_takeout_self.agent.dto.AgentChatRequest;
import com.example.fjm0313_takeout_self.agent.dto.AgentChatResponse;
import com.example.fjm0313_takeout_self.agent.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PutMapping
    @LoginRequired("CUSTOMER")
    public Result<AgentChatResponse> chat(@RequestBody AgentChatRequest request){
        return Result.success(agentService.chat(request));
    }

}
