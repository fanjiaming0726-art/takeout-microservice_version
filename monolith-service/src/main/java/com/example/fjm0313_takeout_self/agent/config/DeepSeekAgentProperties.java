package com.example.fjm0313_takeout_self.agent.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai.deepseek.agent")
public class DeepSeekAgentProperties {

    private String baseUrl;

    private String apikey;

    private String model;

}
